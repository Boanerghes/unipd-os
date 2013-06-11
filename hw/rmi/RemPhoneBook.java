package hw.rmi;

import java.rmi.*;
import java.util.Date;

/**{i}
 * remote date:
 * esempio d'uso di RMI con data remota -
 * interfaccia metodo remotizzato
 *
 * @author M.Moro DEI UNIPD
 * @version 1.00 2003-10-20
 * @version 2.00 2005-10-07 package osExtra
 */

public interface RemPhoneBook extends Remote 
{
    Long get (String name) throws RemoteException;
    
    Long insert (String name, Long number) throws RemoteException;
    
    Long remove (String name) throws RemoteException;
    
    Long waitFor (String name) throws RemoteException;
}