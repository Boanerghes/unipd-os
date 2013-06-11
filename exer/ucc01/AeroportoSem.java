package exer.ucc01;

import os.*;

/** {c}
  * AeroportoSem.java
  * Problema Aeroporto -
  * Avvio torre di controllo Monitor Java
  * e aerei
  * @version 1.00 2003-11-21
  * @author G.Clemente DEI UNIPD
  * @author M.Moro DEI UNIPD
  */

public class AeroportoSem
{
    public static void main (String [] args) 
    {
        System.out.println(
          "---- AEROPORTO CON SEMAFORI PRIVATI ----");
        TorreDiControllo tc = new TorreDiControlloSem();
        for( int i=1 ; i<=20 ; i++) 
        {
            new AereoCheAtterra(i,tc).start();
            Util.rsleep(250, 8000);
            new AereoCheDecolla(20+i,tc).start();
            Util.rsleep(250, 8000);
        }
    }
    
} //{c} AeroportoSem
