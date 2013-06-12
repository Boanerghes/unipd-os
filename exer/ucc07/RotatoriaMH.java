package exer.ucc07;

import os.Monitor;

public class RotatoriaMH extends Rotatoria {
	Monitor m = new Monitor();
	Monitor.Condition[] attesaEst = new Monitor.Condition[4]; // condition for entering vehicles
	Monitor.Condition[] attesaInt = new Monitor.Condition[4]; // condition for moving vehicles
	
	public RotatoriaMH() {
		for (int i=0; i< 4; i++){
			attesaEst[i] = m.new Condition();
			attesaInt[i] = m.new Condition();
		}
	}
	
	@Override
	public int entra(int settore) {
		m.mEnter();
		
		// stop if sector is busy or a car is approaching
		if (occ[settore] || occ[prec(settore)])
			attesaEst[settore].cWait();
		
		occ[settore] = true;
		
		m.mExit();
		return settore;
	}

	@Override
	public int prossimo(int settore) {
		int newSet = succ(settore);
		m.mEnter();
		
		// stop if sector is busy
		if (occ[newSet])
			attesaInt[newSet].cWait();
		
		occ[settore] = false;
		occ[newSet] = true;
		
		// free moving cars first, respecting precedence
		attesaInt[settore].cSignal();
		if (!occ[settore] && !occ[prec(settore)])
			attesaEst[settore].cSignal();
		
		m.mExit();
		return newSet;
	}

	@Override
	public void esce(int settore) {
		m.mEnter();
		
		occ[settore] = false;
		
		// free moving cars first, respecting precedence
		attesaInt[settore].cSignal();
		if (!occ[settore] && !occ[prec(settore)])
			attesaEst[settore].cSignal();
		
		m.mExit();
	}
}
