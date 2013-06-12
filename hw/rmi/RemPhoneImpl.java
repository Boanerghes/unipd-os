package hw.rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.TreeMap;

import os.Semaphore;

public class RemPhoneImpl extends UnicastRemoteObject implements RemPhoneBook
{
	private static Semaphore mutex = new Semaphore(true);
	private static TreeMap <String, Long> phoneBook;
	private static HashMap<String, Semaphore> waiting;
	
	protected RemPhoneImpl() throws RemoteException {
		phoneBook = new TreeMap <String, Long>();
		waiting = new HashMap<String, Semaphore>();
	}

	@Override
	public Long get(String name) throws RemoteException {
		mutex.p();
		Long number = phoneBook.get(name);
		mutex.v();
		
		return number;
	}

	@Override
	public Long insert(String name, Long number) throws RemoteException {
		mutex.p();
		Long old = phoneBook.put(name, number);
		
		// release semaphore for process waiting for name
		if (waiting.containsKey(name)){
			waiting.get(name).v();
		}
		mutex.v();
		
		return old;
	}

	public Long remove(String name) throws RemoteException {
		mutex.p();
		Long old = phoneBook.remove(name);
		mutex.v();
		
		return old;
	}

	public Long waitFor(String name) throws RemoteException {
		Long ret = null;
		Semaphore waiter = new Semaphore();
		
		mutex.p();
		if (phoneBook.containsKey(name))
			waiter.v();
		else {
			if (!waiting.containsKey(name))
				waiting.put(name, waiter);
		}
		mutex.v();
		
		waiter.p();
		
		mutex.p();
		
		ret = phoneBook.get(name);
		
		mutex.v();
		return ret;
	}

	public static void main(String args[]) 
	{
		RemPhoneImpl date;
		try {

			date = new RemPhoneImpl();
			System.out.println("RemRemPhone: bind oggetto");
			Naming.rebind("//localhost/RemPhoneImpl", date);

			System.out.println("Pronto per RMI sull'oggetto");
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
