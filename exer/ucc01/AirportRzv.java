package exer.ucc01;

import os.*;
import os.ada.*;

/** {c}
  * AirportRzv.java
  * Problema Aeroporto -
  * soluzione con Rendez-Vous AdaJava
  * @version 1.00 2012-06-04
  * @author G.Clemente DEI UNIPD
  */
public class AirportRzv {

  public AirportRzv () {
  } //[c]
  
  private class Pilot extends ADAThread{
    protected int id;
    protected String tc;     // torre di controllo
    /**[c]
      * @param id  identificatore del pilota
      * @param tc  rif. torre di controllo
      */
    public Pilot(int id , String tc) {
      super("P"+ id);
      this.id=id;
      this.tc=tc;
    } //[c]

    public void decolla(int N) {
      Util.rsleep(1000, 3000);  // si presenta all'ingresso
      System.out.println(N + ", Rolling req");
      entryCall(null,tc, "richAccessoPista");
      System.out.println(N + ", ok Rolling Request");
      Util.sleep(2000);		      // rullaggio
      entryCall(null,tc, "richAutorizDecollo");
      System.out.println(N + ", ok TakeOff Request");
      Util.sleep(1000);		      // in accelerazione
      entryCall(N,tc, "inVolo");
      System.out.println(N + ", On Air");
    }
    
    public void atterra(int N) {
      System.out.println(N + ", Approaching");
      entryCall(null,tc, "inAvvicinamento");
      System.out.println(N + ", Just for landing");
      entryCall(null,tc, "richAutorizAtterraggio");
      System.out.println(N + ", ok Landing Request");
      Util.rsleep(1000, 4000);  // si allinea con la pista
      entryCall(null,tc, "freniAttivati");
      System.out.println(N + ", Brakes On");
      Util.sleep(3000);		     // rulla sino all'uscita
      entryCall(null,tc, "inParcheggio");
      System.out.println(N + ", Parking");
    }

    public void vola() {
      Util.rsleep(5000, 8000);    // in volo
    }
    
    public void run() {
      while(true) {               // inizia l'attivit�
        Util.rsleep(9500,10500);	// in attesa di missione
        decolla(id);
        vola();
        atterra(id);
      } // while
    }
  } // class Pilot

  private class TorreRzv extends ADAThread{
    protected String name;   	      //
    protected boolean inAtterraggio = false; 	// atterraggio in corso
    protected int occupateA = 0;  	// piste occupate in zona A
    protected int occupateB = 0;  	// piste occupate in zona B
    protected int inArrivo = 0;     // aerei in arrivo
    protected int c = 0;   	        // aerei in volo
    /**[c]
      * @param name  rif torre di controllo
      */
    public TorreRzv(String name) {
      super(name);
      this.name=name;
    } //[c]
  
    public void run() {
      Select request = new Select();

      //***** Soluzione del problema per "componenti" 
      Entry richAccessoPista  = new Entry(){ public Object exec(Object inp) 
        { occupateA++; return null; }};
      Guard RAP = new Guard(){ public boolean when() 
        { return (!inAtterraggio && (inArrivo==0) && (occupateA<2));}};

      Entry richAutorizDecollo  = new Entry(){ public Object exec(Object inp)
        { occupateA--; occupateB++;  return null; }};
      Guard RAD = new Guard(){ public boolean when() 
        { return (occupateB<2);}};

      Entry inVolo = new Entry(){ public Object exec(Object inp) 
        { occupateB--; c++; int N = (Integer) inp;
      System.out.println("Bye " + N);
          return null; }};

      Entry inAvvicinamento = new Entry(){ public Object exec(Object inp) 
        { inArrivo++; return null; }};

      Entry richAutorizAtterraggio  = new Entry(){ public Object exec(Object inp)
        { inArrivo--; occupateA++; inAtterraggio=true; return null; }};
      Guard RAA = new Guard(){ public boolean when() 
        { return ((occupateA+occupateB)==0);}};

      Entry freniAttivati = new Entry(){ public Object exec(Object inp) 
        { occupateA--;occupateB++; return null; }};

      Entry inParcheggio  = new Entry(){ public Object exec(Object inp)
        { inAtterraggio=false; occupateB=0;  c--; return null; }};

      request.add(RAP,"richAccessoPista",richAccessoPista); 
      request.add(RAD,"richAutorizDecollo",richAutorizDecollo); 
      request.add(    "inVolo", inVolo); 
      request.add(    "inAvvicinamento",inAvvicinamento); 
      request.add(RAA,"richAutorizAtterraggio",richAutorizAtterraggio); 
      request.add(    "freniAttivati",freniAttivati); 
      request.add(    "inParcheggio",inParcheggio); 
      System.out.println(name + "accept");

      while(true) {                   // inizia l'attivit� giornaliera
        request.accept();
      } // while
    } //[m] run
  } // class TorreRzv

  public static void main (String [] args) {
    System.out.println("---- AEROPORTO CON R-V in AdaJava ----");
    AirportRzv ap = new AirportRzv();
    ap.new TorreRzv("TC").start();
    System.out.println("---- Hello World - The AirPort is open");
    Util.sleep(1000);  // il server ha tempo per inizializzare gli entry
    for( int i=1 ; i<=5 ; i++) {
      ap.new Pilot(i,"TC").start();
    }
  }
    
} //{c} AirportRzv

