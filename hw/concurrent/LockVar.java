package hw.concurrent;

import java.util.LinkedList;
import java.util.Random;

import os.Semaphore;
import os.Util;

/**
 * An exercise class creating a few threads competing to get 
 * exclusive or shared access on a resource.
 * 
 * Passing a decimal parameter will set the number of competing threads - default is 8.
 * The second integer parameter dictates the number of threads waiting for 
 * a shared lock needed to give absolute precedence to this queue - default is 4.
 * 
 * @author Michele Palmia - 1034792
 */
public class LockVar extends Thread{

	// number of threads currently sharing the resource
	private static int shareCount = 0;
	// the types of lock
	private static enum LockType {
		EXCLUSIVE, SHARED, FREE
	}
	// the state of the lock. free at the start
	private static LockType lock = LockType.FREE;
	// queue of waiting thread's indexes for exclusive or shared locks
	private static LinkedList<Integer> q_ex = new LinkedList<Integer>();
	private static LinkedList<Integer> q_sh = new LinkedList<Integer>();
	// global mutex
	private static Semaphore mutex = new Semaphore(true);
	// private semaphores for the threads
	private static Semaphore priv[];
	
	
	// thread number
	private int idx;
	// when choosing between assigning a shared or exclusive lock
	// if the number of threads waiting for a shared lock is > switchnum, prefer them.
	private static int switchNum;

	/**
	 * Exercise thread, with global/shared lock manager.
	 * @param name the name of the thread
	 * @param i the number (unique) of the thread
	 */
	public LockVar (String name, int i){
		super(name);
		idx = i;
	}

	/**
	 * Request the resource as shared. The request is suspensive.
	 */
	private void requestShared(){
		mutex.p();
		switch (lock) {
		case FREE: case SHARED:
			lock = LockType.SHARED;
			priv[idx].v();
			shareCount++;
			break;

		case EXCLUSIVE:
			q_sh.add(idx);
			System.out.println("<" + getName() + ">" + " queued at position " + q_sh.size() + " for SHARED lock");

			break;
		}
		mutex.v();
		priv[idx].p();
		System.out.println("<" + getName() + ">" + " obtained SHARED lock");
	}

	/**
	 * Releases the resource previously obtained.
	 */
	private void releaseShared(){
		mutex.p();
		shareCount--;
		System.out.println("<" + getName() + ">" + " released SHARED lock");

		if (shareCount == 0){
			System.out.println("<" + getName() + ">" + " ended SHARED lock - (" + shareCount + ")");

			lock = LockType.FREE;
			newAlloc();
		}
		mutex.v();
	}

	/**
	 * Requests the resource with exclusive access. The request is suspensive
	 */
	private void requestExclusive(){
		mutex.p();
		switch (lock) {
		case FREE:
			priv[idx].v();
			lock = LockType.EXCLUSIVE;
			break;
		case EXCLUSIVE: case SHARED :
			q_ex.add(idx);
			System.out.println("<" + getName() + ">" + " queued at position " + q_ex.size() + " for an EXCLUSIVE lock");

			break;
		}
		mutex.v();
		priv[idx].p();
		System.out.println("<" + getName() + ">" + " obtained EXCLUSIVE lock");
	}

	/**
	 * Releases the resource previously obtained.
	 */
	private void releaseExclusive() {
		mutex.p();

		lock = LockType.FREE;
		System.out.println("<" + getName() + ">" + " released EXCLUSIVE lock");
		newAlloc();
		mutex.v();
	}

	/**
	 * Decides to whom allocate the resources when a choice is needed.
	 */
	private void newAlloc() {
		if (q_ex.size() != 0 && q_sh.size() >= switchNum) {
			lock = LockType.SHARED;
			while (null != q_sh.peek()){
				priv[q_sh.poll()].v();
				shareCount++;

			}
		}
		else if (q_ex.size() > 0){
			lock = LockType.EXCLUSIVE;
			priv[q_ex.poll()].v();
		}
	}

	public void run() {
		priv[idx] = new Semaphore();
		
		Random rand = new Random();
		while (true) {
			if (rand.nextBoolean()){
				requestExclusive();
				Util.rsleep(2000, 4000);
				releaseExclusive();
			} else {
				requestShared();
				Util.rsleep(2000, 4000);
				releaseShared();				
			}
			Util.rsleep(4000, 5000);
		}
	}

	public static void main(String[] args){
		int numThread = 8;
		if (args.length >= 1){
			numThread = Integer.parseInt(args[0]);
			System.out.println("using " + args[0] + " threads");
		}
		if (args.length >= 2){
			switchNum = Integer.parseInt(args[1]);
			System.out.println("control switch set to " + args[1]);
		}
		
		priv = new Semaphore[numThread];
		Thread th[] = new Thread[numThread];
		for(int i=0; i<numThread; i++)
			th[i] = new LockVar("t"+i, i);
		System.err.println("** Battere Ctrl-C per terminare!");
		for(int i=0; i<numThread; th[i++].start());
	}
}