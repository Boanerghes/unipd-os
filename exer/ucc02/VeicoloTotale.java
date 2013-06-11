package exer.ucc02;

import os.Util;

public class VeicoloTotale extends Thread {
	private String car;
	private LavaggioAuto la;
	
	public VeicoloTotale (String car, LavaggioAuto la) {
		this.car = car;
		this.la = la;
	}
	
	public void run () {
		Util.rsleep(1000, 40000);
		la.prenotaTotale(car);
		Util.sleep(10000);
		la.lavaInterno(car);
		Util.sleep(6000);
		la.pagaTotale(car);
	}
	
	
}
