   package exer.u0501;
   import os.CountSem;
   import os.Util;

/**{c}
 * unita' 5 esercizio 01 
 * con semaforo a conteggio
 * @author M.Moro DEI UNIPD
 * @version 1.00 2004-10-25
 */
// exE01b 
    public class U0501b extends Thread {
      private static CountSem res = new CountSem(3);
      // risorse
   
    /**[c]
     * thread per esercizio U0501b
     * con semafori con conteggio
     * @param name  nome del thread
     */
       public U0501b(String name) {
         super(name);
      } 
    
    /**[m]
     * ciclo acquisizione
     */
       public void run(){ 
         int cnt = 1;
         System.out.println("Attivato "+getName());
         while(true) {
            res.p(2);
              // acquisice 2 risorse
            System.out.println("<"+getName()+"> "+(cnt++) 
               +" usa una coppia");
            Util.rsleep(500, 2000);
            // rilascio
            res.v(2);
         } // while(true)
      }
   
    /**[m][s]
     * main di collaudo
     */
       public static void main(String[] args) {
         Thread th[] = new Thread[3];
         for(int i=0; i<3; i++)
            th[i] = new U0501b("t"+i);
         System.err.println("** Battere Ctrl-C per terminare!");
         for(int i=0; i<3; th[i++].start());
         while(true) {
            Util.sleep(8000);
            System.out.println("Stato res libere="+res.value());
         } // while
      } //[m][s] main
    
   } //{c} U0501b