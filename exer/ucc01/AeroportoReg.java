package exer.ucc01;

import os.*;

/** {c}
  * AeroportoReg.java
  * Problema Aeroporto -
  * Avvio torre di controllo Regione critica e aerei
  * @version 1.00 2001-03-28
  * @version 2.00 2003-11-22
  * @author S.Cecchin Ist. Negrelli Feltre
  * @author M.Moro DEI UNIPD
  */

public class AeroportoReg {
  public static void main (String [] args) {
    System.out.println("---- AEROPORTO CON REGIONE CRITICA ----");
    TorreDiControlloReg tc = new TorreDiControlloReg();
    for( int i=1 ; i<=20 ; i++) {
      new AereoCheAtterra(i,tc).start();
      Util.rsleep(250, 8000);
      new AereoCheDecolla(20+i,tc).start();
      Util.rsleep(250, 8000);
    }
  }
    
} //{c} AeroportoReg
