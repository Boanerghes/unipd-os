package exer.u0501;
import java.util.*;
import os.Semaphore;
import os.Util;

/**{c}
 * unita' 5 esercizio 01 
 * con semaforo binari e privati
 * @author M.Moro DEI UNIPD
 * @version 1.00 2004-10-25
 */

public class U0501a extends Thread  {
	private static boolean res1 = true, res2=true, res3=true;
	// risorse
	private static int use[];
	// coppia in uso 
	private static LinkedList li = new LinkedList();
	// coda degli indici dei thread in attesa
	private static int waiting = 0;
	// numero dei thread in attesa
	private static Semaphore mutex = new Semaphore(true);
	// mutex unico
	private static Semaphore priv[];
	// semaforo privato del thread
	private int idx;
	// indice del thread

	/**[c]
	 * thread per esercizio e01a
	 * con semafori privati
	 * @param name  nome del thread
	 * @param i  indice del thread
	 */
	public U0501a(String name, int i){
		super(name);
		idx = i;  } 

	/**[m]
	 * verifica ed allocazione delle risorse -
	 * le risorse possono essere tutte e tre libere
	 * oppure una sola
	 * @return 1 se allocate 1-2, 2 se 1-3, 3 se 2-3
	 */
	private int alloc(){
		int ret;

		if (res1 && res2 && res3) {
			// caso di scelta casuale
			ret = Util.randVal(1,3);
			switch(ret){
			case 1: // 1 e 2
				res1 = res2 = false;
				break;
			case 2: // 1 e 3
				res1 = res3 = false;
				break;
			case 3: // 2 e 3
				res2 = res3 = false;
				break;
			}
			return ret;
		}
		// non c'e' una coppia libera
		return 0;
	}

	/**[m]
	 * ciclo acquisizione/rilascio
	 */
	public void run() {
		int cnt = 1, ret;
		String s = "";

		priv[idx] = new Semaphore();
		// creato dal thread come privato
		System.out.println("Attivato "+getName());
		while(true){
			mutex.p();
			if ((use[idx] = alloc()) != 0) {
				// allocazione eseguita
				System.out.println("+++ <"+getName()+"> NON attende");
				priv[idx].v();}
			else {
				System.out.println("--- <"+getName()+"> attende");
				waiting++;
				li.add(new Integer(idx));}
			// enqueue idx corrente 
			mutex.v();
			// eventuale attesa sul proprio semaforo
			priv[idx].p();
			switch(use[idx]) {
			case 1:
				s = "1 e 2";
				break;
			case 2:
				s = "1 e 3";
				break;
			case 3:
				s = "2 e 3";
				break;
			}
			System.out.println("<"+getName()+"> "+(cnt++) +
					" usa la coppia "+s);
			Util.rsleep(600, 2000);
			// rilascio
			mutex.p(); // non sarebbe strettamente necessario
			switch(use[idx]){
			case 1:
				res1 = res2 = true;
				break;
			case 2:
				res1 = res3 = true;
				break;
			case 3:
				res2 = res3 = true;
				break;
			}
			use[idx] = 0;
			// controllo per altri processi
			if (waiting != 0 && (ret = alloc()) != 0) {
				// e' possibile avviare un thread
				waiting --;
				int i = ((Integer)li.removeFirst()).intValue();
				use[i] = ret; priv[i].v();
				// sgancia il thread
			}
			mutex.v();
			Util.rsleep(600, 4000);
		} // while(true)
	}


	/**[m][s]
	 * main di collaudo
	 */
	public static void main(String[] args) {
		// init statico
		// (si poteva fare in un blocco statico)
		use = new int[3];
		priv = new Semaphore[3];     
		Thread th[] = new Thread[3];
		for(int i=0; i<3; i++)
			th[i] = new U0501a("t"+i, i);
		System.err.println("** Battere Ctrl-C per terminare!");
		for(int i=0; i<3; th[i++].start());
		while(true) {
			Util.sleep(8000);
			System.out.println("Stato res1="+res1+ " res2=" +
					res2 + " res3=" + res3);
		} // while
	} //[m][s] main

} //{c} U0501a