package exer.ucc02;

import os.Monitor;

public class LavaggioAutoMdh extends LavaggioAuto {
	
	Monitor m = new Monitor();
	Monitor.Condition accEst = m.new Condition();
	Monitor.Condition accInt = m.new Condition();

	@Override
	public void prenotaParziale(String car) {
		
	}

	@Override
	public void pagaParziale(String car) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prenotaTotale(String car) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lavaInterno(String car) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pagaTotale(String car) {
		// TODO Auto-generated method stub
		
	}

	
	
}
