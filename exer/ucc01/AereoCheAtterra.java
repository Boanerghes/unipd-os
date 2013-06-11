package exer.ucc01;

import os.*;

/** {c}
  * AereoCheAtterra.java
  * Problema Aeroporto -
  * Un aereo che atterra -
  * Ha priorita` sul decollo e richiede l'intera pista libera
  * @version 1.00 2001-03-28
  * @version 2.00 2003-11-21
  * @author S.Cecchin Ist. Negrelli Feltre
  * @author M.Moro DEI UNIPD
  */

public class AereoCheAtterra extends Thread {
  private int io ;
  private TorreDiControllo tc;

  /**[c]
    * @param io  indice del volo
    * @param tc  rif. torre di controllo
    */
  public AereoCheAtterra(int io , TorreDiControllo tc) {
    this.io=io;
    this.tc=tc;
  }
  
  public void run() {
    Util.rsleep(5000, 8000);		// in volo
    tc.richAutorizAtterraggio(io);
    Util.rsleep(1000, 4000);		// si allinea con la pista
    tc.freniAttivati(io);
    Util.rsleep(1000, 4000);		// rulla sino all'uscita
    tc.inParcheggio(io);			// libera la pista
  }

} // class AereoCheAtterra
