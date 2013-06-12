package hw.rmi;

import java.rmi.*;
import java.util.Date;

public interface RemPhoneBook extends Remote 
{
    Long get (String name) throws RemoteException;
    
    Long insert (String name, Long number) throws RemoteException;
    
    Long remove (String name) throws RemoteException;
    
    Long waitFor (String name) throws RemoteException;
}