package exer.ucc01;

import os.ada.*;

/** {c}
  * TorreDiControlloRzv.java
  * Problema Aeroporto -
  * Torre di controllo versione
  
  * con Ada-Java
  * @version 1.00 2013-05-28
  * @author G.Clemente DEI UNIPD
  * @author M.Moro DEI UNIPD
  */

public class TorreDiControlloRzv extends TorreDiControllo implements Runnable {
    private String name;
      // nome del thread
    private ADAThread tc;
      // il thread server
      
    /**[c]
      * @param name  rif torre di controllo
      */
    public TorreDiControlloRzv(String name) {
      tc = new ADAThread(name);
      this.name=name;
    } //[c]
  
    /**[m]
      * il server
      */
    public void run() {
      ADAThread.Select request = tc.new Select();

      //***** Soluzione del problema per "componenti" 
      Entry richAccessoPista  = new Entry(){ public Object exec(Object inp) 
        { occupateA++; return null; }};
      Guard RAP = new Guard(){ public boolean when() 
        { return (!att && (tc.entryCount("richAutorizAtterraggio")==0) 
          && (occupateA<2));}};

      Entry richAutorizDecollo  = new Entry(){ public Object exec(Object inp)
        { occupateA--; occupateB++;  return null; }};
      Guard RAD = new Guard(){ public boolean when() 
        { return (occupateB<2);}};

      Entry inVolo = new Entry(){ public Object exec(Object inp) 
        { occupateB--; contaDec++; int N = (Integer) inp;
          System.out.println("Bye " + N);
          return null; }};

      Entry richAutorizAtterraggio  = new Entry(){ public Object exec(Object inp)
        { occupateA++; att=true; return null; }};
      Guard RAA = new Guard(){ public boolean when() 
        { return ((occupateA+occupateB)==0);}};

      Entry freniAttivati = new Entry(){ public Object exec(Object inp) 
        { occupateA--;occupateB++; return null; }};

      Entry inParcheggio  = new Entry(){ public Object exec(Object inp)
        { att=false; occupateB=0; contaAtt++; return null; }};

      request.add(RAP,"richAccessoPista",richAccessoPista); 
      request.add(RAD,"richAutorizDecollo",richAutorizDecollo); 
      request.add(    "inVolo", inVolo); 
      request.add(RAA,"richAutorizAtterraggio",richAutorizAtterraggio); 
      request.add(    "freniAttivati",freniAttivati); 
      request.add(    "inParcheggio",inParcheggio); 
      System.out.println(name + " accept");

      while(true) {                   // inizia l'attivit� giornaliera
        request.accept();
        stampaSituazioneAeroporto();

      } // while
    } //[m] run

    public void richAutorizAtterraggio(int io) {
        System.out.println(
          "$$ L'aereo A"+io+" !!!!!!!! RICHIESTA AUTORIZZAZIONE ATTERRAGGIO");
        tc.entryCall(null, "richAutorizAtterraggio");
        System.out.println("$$$$ L'aereo A"+io+" IN FASE DI ATTERRAGGIO ");
    }

    public void freniAttivati(int io){
        tc.entryCall(null, "freniAttivati");
        System.out.println("$$$$$$ L'aereo A"+io+" TOCCA TERRA, FRENA ");
    }

    public void inParcheggio(int io) {
        tc.entryCall(null, "inParcheggio");
        System.out.println("$$$$$$$$ L'aereo A"+io+" LIBERA LA PISTA E PARCHEGGIA");
    }

    public void richAccessoPista(int io) { 
        System.out.println(
          "** L'aereo D"+io+" ^^^^^^^^ RICHIESTA PISTA PER DECOLLO");
        tc.entryCall(null, "richAccessoPista");
        System.out.println("**** L'aereo D"+io+ " SI PREPARA AL DECOLLO");
  }

    public void richAutorizDecollo(int io) {
        System.out.println(
          "****** L'aereo D"+io+" RICHIEDE AUTORIZZAZIONE DECOLLO");
        tc.entryCall(null, "richAutorizDecollo");
        System.out.println("******** L'aereo D"+io+" IN FASE DI DECOLLO ");
    }

    public void inVolo(int io) {
        tc.entryCall(io, "inVolo");
        System.out.println("********** L'aereo D"+io+ " HA PRESO IL VOLO!!!!! ");
    }

} //{c} TorredDiControlloRzv
