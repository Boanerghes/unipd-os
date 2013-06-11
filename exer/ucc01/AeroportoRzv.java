package exer.ucc01;

import os.*;
import os.ada.*;

/** {c}
  * AeroportoRzv.java
  * Problema Aeroporto -
  * Avvio torre di controllo RendezVous in Java e aerei
  * @version 1.00 2011-11-21
  * @author G.Clemente DEI UNIPD
  * @author G.Clemente DEI UNIPD
  */

public class AeroportoRzv {

  public static void main (String [] args){
    System.out.println("---- AEROPORTO CON RENDEZ-VOUS ----");
    TorreDiControlloRzv tc = new TorreDiControlloRzv("TC");
    Thread tcth = new Thread(tc, "TC");
    tcth.start();
    for( int i=1 ; i<=20 ; i++) {
      Runnable aar = new AereoCheAtterra(i,tc);
      new ADAThread(aar, "AA"+i).start();
      Util.rsleep(250, 8000);
      aar = new AereoCheDecolla(20+i,tc);
      new ADAThread(aar, "AD"+i).start();
      Util.rsleep(250, 8000);
    }
  }
} //{c} AeroportoRzv
