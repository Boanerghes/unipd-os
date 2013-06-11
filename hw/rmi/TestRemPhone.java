package hw.rmi;

import java.util.Date;
import java.rmi.*;
import os.Sys;

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

public class TestRemPhone
{
	/**[m][s]
	 * main di collaudo
	 */
	public static void main(String args[])  {
		RemPhoneBook book = null;
		try {
			// Naming.lookup("rmi:///RemDateImpl");
			// remDate = (RemDate) Naming.lookup("///RemDateImpl");

			book = (RemPhoneBook)((args.length == 0) ?
					Naming.lookup("//localhost/RemPhoneImpl") :
						Naming.lookup("//"+args[0]+"/RemPhoneImpl"));
			
			System.out.println("inserting Marco - 3408340884");
			book.insert("Marco", 3408340884L);
			
			System.out.println("inserting Giovanni - 3489738532");
			book.insert("Giovanni", 3489738532L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} //[m][s] main

} //{c} TestRemDate
