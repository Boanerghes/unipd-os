//tentativo di soluzione alla prova di programmazione in ADA-Java
//della prova scritta del 20 giugno 2012 utilizzando le macro

/**alcune specifiche del problema:
esiste un'unica uscita U dove non è prevista attesa;
se l'ingresso S è aperto, entrano nell'ordine due persone da P e una da S, posto
che S non sia vuota.
Se l'ingresso S è chiuso, la prenotazione su S non è consentita
*/

import os.*
import os.ada*

public class Salone
{
  
  private static int APRI = 10;//numero di persone
  //all'ingresso Principale P per cui si apre l'ingresso Speciale S
  private static int CHIUDI = 3;//numero di persone all'ingresso principale
  //P per cui si chiude l'ingresso Speciale S
  private static int MAXP = 10;//numero massimo di persone che possono
  //stare nell'area espositiva
  private int n_persone = 0;//contatore delle persone all'interno del salone
  private int numP = 0;//contatore di persone in attesa all'ingresso P
  private int numS = 0;//contatore di persone in attesa all'ingresso S
  private boolean apertoS = false; //indica se l'ingresso S è aperto o meno
  //l'ingresso P è sempre aperto
  private long timeout = 10000L;//tempo che una persona è disposta ad attendere 
  //in coda
  private String salone;//nome del Thread server
  private int dapassare=2;//indica quanti da p devono ancora entrare prima
  //di poter far entrare uno da s
  
  //{c} della classe salone
  private Salone()
  {}
  
  private class Persona extends ADAThread
  {
    private int id;
    private int minT;
    private int maxT;
    private boolean avvenuto;//indica se l'ingresso è avvenuto o meno
    
    //costruttore del thread Persona
    public Persona(int i, int min, int max)
    {
      super("persona "+i);
      id=i;
      minT=min;
      maxT=max;
    }//fine costruttore
    
    //metodo run
    public void run()
    {
      Util.rsleep(minT, maxT);
      //la persona si prenota agli ingressi, a P se S è chiuso, da S altrimenti
      avvenuto = entra(timeout);
      if(!avvenuto)
      	{
      	 System.out.println("attenzione, la persona " + id +
      	   " si è stancata di aspettare e se ne va via");
      	}
      else//la persona è entrata e dopo quello che defe fare se ne esce
      	{
      	  Util.rsleep(minT, maxT);//simula la permanenza della persona nel salone
      	  uscita();
      	}
      	
    }//fine del metodo run
    
    //effettua la prenotazione su una delle due code e quindi effettua l'ingresso
    public boolean entra(long timeout)
    {
      int r = Util.randVal(0, 65);//sceglie a caso tra P e S
      CallOut out;//dirà se S è aperto o meno 
      bollean P;//dice se si è scelto effettivamente di stare in coda su P o su S
      	  if(r>35)//scelgo S
      	    {
      	      //scegliendo S si trovano 2 casi: 1 è aperto e tutto
      	      //fila liscio, 2 è chiuso, quindi si ripiega su P
      	     out = entryCall(false, salone, "prenota");
      	     P=false;
      	     boolean aperto = (boolean)out.getParams();//non sono troppo sicuro di
      	     //questa riga, si può scrivere così?
      	     if(!aperto)
      	       {
      	         System.out.println("attenzione, la persona " + id +
      	           " aveva scelto l'ingresso chiuso S, è ripiegato su P");
      	         entryCall(true, salone, "prenota");//entro allora da P
      	         P=true;
      	       }
      	    }//fine if se scelgo S
      	  else//scelgo P
      	    {
      	      entryCall(true, salone, "prenota");
      	      P=true;
      	    }
      	   //in ogni caso, bisogna verificare se la persona si è stancata o meno
      if(P)
      	out = entryCall(null, salone, inP, timeout);
      else
      	out = entryCall(null, salone, inS, timeout);
      
      if(out.getTimeout()!=Timeout.EXPIRED)//l'ingresso è avvenuto con successo
      	{
      	  return true;
      	}
      else return false;//il cliente si è stancato di aspettare in coda	
    }//fine del metodo entra
    
    
    public void esce()
    {
     entryCall(null, salone, "uscita");//semplice semplice
    }//fine del metodo esce
    
    
  }//fine del thread Persona
  
  //{c} del task di servizio Esp che fa da server per i client che vogliono entrare
  private class Esp extends ADAThread
  {
    public Esp(String nome)
    {
      super(nome);
      salone=nome;
    }
    
    public void run()//metodo run del server, qui dentro il select
    {
     Select select = new Select();
     
     select.add(=>//sempre vero
       "prenota"[in: boolean princ; out: boolean ok;]
       {
         if(princ)//ci è chiesto di entrare da P
           {
             //il client si mette in attesa sulla coda P
             numP++;
             if(numP>=APRI)//se siamo in troppi in P, ci si aiuta con S
               {
                 apertoS=true;
                 ok=false;
               }//fine if intero
           }//fine if esterno
           else//ci è chiesto di usare S
             {
               if(apertoS)//prima bisogna essere certi che S sia aperto
               	 {
               	   numS++;
               	   ok=true;
               	 }
               else
               	 ok=false;//non abbiamo potuto usare S
             }
       });//fine entry prenota
     select.add(=>//sempre aperta
       "rinuncia"[in:boolean princ]
       {
         if(princ)//ci è chiusto di rinunciare all'attesa in P
           {
            numP--;
           }
          else
            numS--;
       });//fine entry rinuncia
     select.add(when(n_persone<MAX && dapassare!=0)//si entra solo se c'è spazio
     "inP"[],
     {
       n_persone++;
       numP--;
       if(numc>0)
         dapassare--;
       if(numP<=CHIUDI)//se siamo rimasti in pochini in P, si può anche chiudere S
       	 apertoS=false;
     });//fine entry inP
     select.add(when(n_persone<MAX && dapassare==0)
       "inS"[],
       {
        numS--;
        n_persone++;
        dapassare=2; 
       })//fine entry inS
     select.add(=>//guardia sempre aperta
       "uscita"[]
       {
         n_persone--;
       });//fine entry uscita
     //fine delle dichiarazioni degli accetp nel select
     
     //faccio girare il server:
     when(true)
     {
       select.accept();
     }
    }//fine del metodo run
  }//fine della classe che funge da server
  
  //main di collaudo
  public static void main(String[] args)
  {
    Salone s = new Salone();
    s.new Ep("salone").start();//do il via al server
    Util.sleep(1000);
    for(int i =0, i<50; i++)
      {
        s.new Persona.start();
      }
  }
}//fine classe esterna Salone
