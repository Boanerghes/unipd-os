package hw.rmi;

import hw.concurrent.LockVar;

import java.util.Date;
import java.rmi.*;
import os.Sys;
import os.Util;

/**{c}
 * remote date:
 * esempio d'uso di RMI con data remota -
 * file di test
 *
 * @author M.Moro DEI UNIPD
 * @version 1.00 2003-10-20
 * @version 2.00 2005-10-07 package os e osExtra
 * @version 2.01 2013-05-08 generalizz. indirizzo
 */

public class TestConcurrentRemDate extends Thread {
	private static RemDate remDate;
	
	public TestConcurrentRemDate (String name) {
		super(name);
	}
	
	public void run() {
		System.out.println("<" + getName() + ">" + " running");
		Util.rsleep(0, 5000);
		Date d = null;
		try { d = remDate.getDate(); }
		catch (Exception e) { e.printStackTrace(); }
		
		System.out.println("<" + getName() + ">" + " received " + d);
		
	}
	
	/**[m][s]
	 * main di collaudo
	 */
	public static void main(String args[]) 
	{
		Date d1 = new Date();
		remDate = null;
		System.out.println("TestRemDate: tenta lookup su: "+ ((args.length == 0) ?
				"//localhost/RemDateImpl" :
					"//"+args[0]+"/RemDateImpl"));
		try {            
			remDate = (RemDate)((args.length == 0) ?
					Naming.lookup("//localhost/RemDateImpl") :
						Naming.lookup("//"+args[0]+"/RemDateImpl"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int numThread = 3;
		
		Thread th[] = new Thread[numThread];
		for(int i=0; i<numThread; i++)
			th[i] = new TestConcurrentRemDate("t"+i);
		for(int i=0; i<numThread; th[i++].start());
		
		Util.sleep(6000);
		
		String unb = Sys.in.readLine("Unbind l'oggetto (y,N)? ");
		if (unb.length() != 0 &&
				Character.toUpperCase(unb.charAt(0)) == 'Y')
			try {
				remDate.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	} //[m][s] main

} //{c} TestRemDate
