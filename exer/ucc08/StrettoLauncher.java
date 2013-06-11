package exer.ucc08;

import os.Util;
import exer.ucc08.Stretto.Side;

public class StrettoLauncher {
	public static void main (String[] args) {
		StrettoRzv st = new StrettoRzv("ST");
		Thread stth = new Thread(st, "ST");
		stth.start();
		
		Util.sleep(2000);
		
		for	(int i=0; i<5; i++) {
			new Barca (i, st, Side.A).start();
		}

		Util.sleep(7000);
		
		for( int i=5 ; i<=15 ; i++) {
			
			if (i%2 == 0){
				new Barca(i,st, Side.A).start(); }
			else {
				new Barca(i,st, Side.B).start(); }
		}
	}


}
