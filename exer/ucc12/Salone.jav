//tentativo di soluzione alla prova di programmazione in ADA-Java
//della prova scritta del 20 giugno 2012 utilizzando le macro

/**alcune specifiche del problema:
esiste un'unica uscita U dove non e' prevista attesa;
se l'ingresso S e' aperto, entrano nell'ordine due persone da P e una da S, posto
che S non sia vuota.
Se l'ingresso S e' chiuso, la prenotazione su S non e' consentita
*/

import os.*
import os.ada*

public class Salone {

  private static int APRI = 10;//numero di persone
  //all'ingresso Principale P per cui si apre l'ingresso Speciale S
  private static int CHIUDI = 3;//numero di persone all'ingresso principale
  //P per cui si chiude l'ingresso Speciale S
  private static int MAXP = 10;//numero massimo di persone che possono
  //stare nell'area espositiva
  
  private class Persona extends ADAThread {
    private int id;
    private String salone;//nome del Thread server
    
    //costruttore del thread Persona
    public Persona(int i, String salone)
    {
      super("persona "+i);
      this.id=i;
      this.salone = salone;
    }//fine costruttore

    //effettua la prenotazione su una delle due code e quindi effettua l'ingresso
    public boolean entra(long timeout)
    {
      int r = Util.randVal(0, 100);//sceglie a caso tra P e S
      CallOut out;//dir se S e' aperto o meno 
      boolean P; //dice se si e' scelto effettivamente di stare in coda su P o su S
      if( r < 35) { // scelgo S
      	      //scegliendo S si trovano 2 casi: 1 e' aperto e tutto
      	      //fila liscio, 2 e' chiuso, quindi si ripiega su P
        out = entryCall(false, salone, "prenota");
        P=false;
        // autoboxing a sinistra dell'uguale, ma il cast va fatto su oggetti
      	boolean aperto = (Boolean) out.getParams();
      	if(!aperto) {
          entryCall(true, salone, "prenota");//entro allora da P
      	  P=true;
      	}
      }//fine if se scelgo S
      else { // scelgo P
        entryCall(true, salone, "prenota");
        P=true;
      }
      //in ogni caso, bisogna verificare se la persona si e' stancata o meno
      if(P) out = entryCall(null, salone, inP, timeout);
      else out = entryCall(null, salone, inS, timeout);

      if(out.getTimeout() != Timeout.EXPIRED)//l'ingresso e' avvenuto con successo
        return true;
      else return false;//il cliente si e' stancato di aspettare in coda	
    }//fine del metodo entra
    
    
    public void esce()
    {
     entryCall(null, salone, "uscita");//semplice semplice
    }//fine del metodo esce
  }//fine del thread Persona
  
  //{c} del task di servizio Esp che fa da server per i client che vogliono entrare
  private class Esp extends ADAThread {

    private int n_persone = 0;//contatore delle persone all'interno del salone
    private int numP = 0;//contatore di persone in attesa all'ingresso P
    private int numS = 0;//contatore di persone in attesa all'ingresso S
    private boolean apertoS = false; //indica se l'ingresso S e' aperto o meno
    //l'ingresso P e' sempre aperto
    private int dapassare=2;//indica quanti da p devono ancora entrare prima
    //di poter far entrare uno da s
  

    public Esp(String nome)
    {
      super(nome);
    }

    public void run() { //metodo run del server, qui dentro il select 
      Select select = new Select();

      select.add(=>//sempre vero
        "prenota"[in: boolean princ; out: boolean ok;]
        {
          ok = true;
          if(princ)//ci e' chiesto di entrare da P
          {
            //il client si mette in attesa sulla coda P
            numP++;
            if(numP>=APRI)//se siamo in troppi in P, ci si aiuta con S
            {
              apertoS=true;
            }//fine if intero
          }//fine if esterno
          else//ci e' chiesto di usare S
          {
            if(apertoS)//prima bisogna essere certi che S sia aperto
            {
              numS++;
            }
            else
              ok=false;//non abbiamo potuto usare S
            }
        }
      );//fine entry prenota
      select.add(=>//sempre aperta
        "rinuncia"[in:boolean princ]
        {
          if(princ)//ci e' chiusto di rinunciare all'attesa in P
          {
            numP--;
            if (numP<CHIUDI){ apertoS = false; }
          }
          else
            numS--;
        }
      );//fine entry rinuncia
      select.add(when(n_persone<MAX && dapassare!=0)//si entra solo se c'e' spazio
        "inP"[],
        {
          n_persone++;
          numP--;
          if(numS>0)
            dapassare--;
          else dapassare = 2; // per sicurezza
          if(numP<=CHIUDI)//se siamo rimasti in pochini in P, si pu˜ anche chiudere S
          apertoS=false;
        }
      );//fine entry inP
      select.add(when(n_persone<MAX && dapassare==0)
        "inS"[],
        {
          numS--;
          n_persone++;
          dapassare=2; 
        }
      );//fine entry inS
      select.add(=>//guardia sempre aperta
        "uscita"[]
        {
          n_persone--;
        }
      );//fine entry uscita
     //fine delle dichiarazioni degli accetp nel select
     
     //faccio girare il server:
      when(true)
      {
        select.accept();
      }
    }//fine del metodo run
  }//fine della classe che funge da server
}//fine classe esterna Salone
