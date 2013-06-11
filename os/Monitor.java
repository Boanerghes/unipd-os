package os;

/**{c}
 * Monitor di Hoare
 * @author M.Moro DEI UNIPD
 * @version 1.00 2002-04-17
 * @version 2.00 2003-10-03 package Os
 * @version 2.01 2003-11-22 Condition public
 * @version 2.02 2004-11-09 timeout con timer
 * @version 2.03 2005-10-07 package os
 * @version 2.04 2005-10-28 aggiunto Condition.queue()
 */

public class Monitor
{
    private MutexSem mutex = new MutexSem();
    private Semaphore urgent = new CountSem(0, false);
      // con coda LIFO, rimane sempre a 0
    private int urgentCount=0;
      // processi sul semaforo urgent
    private long elapsed;
      // tempo rimasto dal timeout:
      // la semantica del Monitor di Hoare
      // garantisce che questa variabile
      // venga scritta dal timer e subito dopo letta
      // dal thread che si risveglia

    /**{c}
     * condition di Monitor
     * @author M.Moro DEI UNIPD
     * @version 1.0 2002-04-17
     */
    public class Condition
    {
       private Semaphore cond = new Semaphore(0,1);
         // semaforo binario d'attesa, sempre rosso
       private int condCount=0;
         // conteggio thread in attesa

        /**[m]
         * costruttore pubblico
         */
        public Condition()
        {}        
          
        /**[m]
         * wait sul condition
         */
        public void cWait()
        {
            condCount++;
            if( urgentCount>0 )
                urgent.v();
            else
                mutex.v();
            cond.p();
            condCount--;
        } //[m] cWait

        /**[m]
         * wait sul condition con timeout
         * @param timeout  massimo tempo di attesa in ms
         *                 NOTIMEOUT senza timeout
         *                 IMMEDIATE se sincronizzazione immediata richiesta
         * @return tempo rimasto rispetto al timeout -
         *         timeout se non sospensivo o INTIME -
         *         INTIME<ret<=timeout se v() arriva in tempo
         *         EXPIRED se spirato
        */
        public long cWait(long timeout)
        {
            elapsed = timeout;
            
            Timer tm = new Timer(new TimerCallback()
            {
                // classe anonima di callback del timer
                /**[m]
                 * @see TimerCallback#call
                 */
                public void call()
                {
                    // timeout scaduto
                    mEnter();
                    elapsed = Timeout.EXPIRED;
                    cSignal();
                    mExit();
                } //[m] call

                /**[m]
                 * @see TimerCallback#toString
                 */
                public String toString()
                { return "Monitor timeout Timer"; }
            }, timeout);
            
            tm.start();  // attiva il timer
            cWait();  // attesa sul Condition
            
            // al risveglio potrebbe essere pervenuto
            // un signal da un thread normale o dal timer
            // se timeout spirato
            if (elapsed != Timeout.EXPIRED)
            {
                // non scaduto, signal normale
                elapsed=tm.getVal();  // tempo rimasto
                tm.cancel();  // disattiva timer
            } 
            return elapsed;
        } //[m] cWait(timeout)
        
        /**[m]
         * signal sul condition
         */
        public void cSignal()
        {
            if (condCount>0)
            {
                // ci sono thread in attesa sul condition
                urgentCount++;
                cond.v();  // risveglio
                urgent.p();  // attesa del risvegliante
                urgentCount--;
            }
        } //[m] cSignal

        /**[m]
         * thread in attesa 
         * @return numero di thread in attesa sul condition
        */
        public int queue()
        {  return condCount; }
        
    } //{c} Condition

    /**[m]
     * ingresso del monitor
     */
    public void mEnter()
    {
       mutex.p();
    }

    /**[m]
     * uscita dal monitor
     */
    public void mExit()
    {
       if (urgentCount > 0)
           urgent.v();
       else
           mutex.v();
    }

} //{c} Monitor
