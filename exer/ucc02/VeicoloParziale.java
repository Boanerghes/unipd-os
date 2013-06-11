package exer.ucc02;

import os.Util;

public class VeicoloParziale extends Thread {

	private String car;
	private LavaggioAuto la;
	
	public VeicoloParziale (String car, LavaggioAuto la) {
		this.car = car;
		this.la = la;
	}
	
	@Override
	public void run () {
		Util.rsleep(1000, 40000);
		la.prenotaParziale(car);
		Util.sleep(7000);
		la.pagaParziale(car);
	}
}
