package exer.ucc08;

import os.Util;
import os.ada.ADAThread;

public class Barca extends ADAThread{
	private int io;
	private StrettoRzv sc;
	Stretto.Side position;
	
	public Barca (int io, StrettoRzv sc, Stretto.Side position){
		super(String.valueOf(io));
		
		this.io = io;
		this.sc = sc;
		this.position = position;
	}
	
	public void run(){		
		if (position.equals(Stretto.Side.A)){ 
			sc.entraAB(io); }
		else { 
			sc.entraBA(io);}
		
		Util.sleep(5000);
		
		if (position.equals(Stretto.Side.A)) {
			sc.esceAB(io); }
		else { 
			sc.esceBA(io); }
	}
}
