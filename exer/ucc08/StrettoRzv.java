package exer.ucc08;

import os.ada.ADAThread;
import os.ada.Entry;
import os.ada.Guard;

public class StrettoRzv extends Stretto implements Runnable {
	private String name;
	private ADAThread tc;

	public StrettoRzv(String name) {
		tc = new ADAThread(name);
		this.name=name;
	}

	@Override
	public void run() {
		ADAThread.Select request = tc.new Select();

		//***** Soluzione del problema per "componenti" 
		Entry entraAB  = new Entry(){ 
			public Object exec(Object inp) {
				passing++;
				
				if ((tc.entryCount("entraAB") == 0 && tc.entryCount("entraBA") != 0) || tc.entryCount("entraBA") == 1) {
					side = Side.B;
					sCounter = 0;
				}
				else {
					if (side.equals(Side.N)){
						side = Side.A; sCounter = 1;
					}
					else if (side.equals(Side.A)){ 
						sCounter++; 
						if (sCounter == 2) {
							side = Side.B;
							sCounter = 0;
						}
					}
				}
				return null; 
			}
		};
		Guard GA = new Guard(){
			public boolean when() { 
				return ((side == Side.N || side == Side.A) && passing < 2);
			}
		};

		Entry entraBA  = new Entry(){ 
			public Object exec(Object inp) {
				passing++;
				
				if ((tc.entryCount("entraBA") == 0 && tc.entryCount("entraAB") != 0) || tc.entryCount("entraAB") == 1) {
					side = Side.A;
					sCounter = 0;
				}
				else {
					if (side.equals(Side.N)){
						side = Side.B; 
						sCounter = 1;
					}
					else if (side.equals(Side.B)){ 
						sCounter++; 
						if (sCounter == 2) {
							side = Side.A;
							sCounter = 0;
						}
					}
				}
				return null; 
			}
		};
		
		Guard GB = new Guard(){
			public boolean when() { 
				return ((side == Side.N || side == Side.B) && passing < 2);
			}
		};

		Entry esceAB = new Entry(){
			public Object exec(Object inp) {
				totBarche++;  passing--;
				if (tc.entryCount("entraAB") == 0 || tc.entryCount("entraBA") == 0){ side = Side.N; sCounter = 0;} 
				return null; 
			}
		};

		Entry esceBA = new Entry(){ 
			public Object exec(Object inp) {
				totBarche++;  passing--;
				if (tc.entryCount("entraAB") == 0 || tc.entryCount("entraBA") == 0){ side = Side.N; sCounter = 0;} 
				return null; 
			}
		};

		request.add(GA,"entraAB",entraAB); 
		request.add(GB,"entraBA",entraBA); 
		request.add(   "esceAB" ,esceAB);
		request.add(   "esceBA" ,esceBA);
		request.add(2000L);
		
		System.out.println(name + " accept");

		while(true) {
			printStrettoStats();
			request.accept();
		}

	}

	private void printStrettoStats () {
		String status = "q - entraAB: " + tc.entryCount("entraAB") + ", entraBA: " + tc.entryCount("entraBA") + ", esceAB: " + tc.entryCount("esceAB") + ", esceBA: " + tc.entryCount("esceBA") + "---" + "passing: " + passing + ", tot: " + totBarche + ", side: " + side + ", sCounter: " + sCounter;
		System.out.println(status);
	}

	@Override
	public void entraAB(int io) {
		System.out.println("?????? - "  + io + " queueing for entrance A -> B");
		tc.entryCall(null, "entraAB");
		System.out.println("!!!!!! - "  + io + " entered in A -> B");
	}

	@Override
	public void entraBA(int io) {
		System.out.println("?????? - "  + io + " queueing for entrance B -> A");
		tc.entryCall(null, "entraBA");
		System.out.println("!!!!!! - "  + io + " entered in B -> A");
	}

	@Override
	public void esceAB(int io) {
		System.out.println("?????? - "  + io + " ready to get out A -> B");
		tc.entryCall(null, "esceAB");
		System.out.println("!!!!!! - "  + io + " got out A -> B");
	}

	@Override
	public void esceBA(int io) {
		System.out.println("?????? - "  + io + " ready to get out B -> A");
		tc.entryCall(null, "esceBA");
		System.out.println("!!!!!! - "  + io + " got out B -> A");
	}
}	