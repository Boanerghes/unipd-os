package exer.ucc01;

import os.*;

/** {c}
  * Aeroporto.java
  * main generale
  * @version 1.00 2005-11-18
  * @author M.Moro DEI UNIPD
  */
public class Aeroporto {
  private static void usage() {
    System.out.println("uso: Aeroporto <n> \n" + 
      "n=1 Mdh Monitor di Hoare\n"+
      "n=2 Mdj Monitor di Java\n"+
      "n=3 Reg Regioni Critiche\n"+
      "n=4 Rzv Rendez-Vous in java\n"+
      "n=5 Sem Semafori Privati\n");
 }
  public static void main (String [] args) {
    System.out.println("**** PROBLEMA AEROPORTO IN JAVA ****");
    usage();
    int ch = Sys.in.readInt("--   inserire n");

    try {
      switch(ch) {
        case 1:
          AeroportoMdh.main(args); break;
        case 2:
          AeroportoMdj.main(args); break;
        case 3:
          AeroportoReg.main(args); break;
      case 4:
          AeroportoRzv.main(args); break;
        case 5:
          AeroportoSem.main(args); break;
        default:
          usage(); break;
      }
    } 
    catch(NumberFormatException e) { usage(); }
  } //[m][s] main

} //{c} Aeroporto
