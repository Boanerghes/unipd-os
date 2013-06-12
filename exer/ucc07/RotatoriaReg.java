package exer.ucc07;

import os.Region;
import os.RegionCondition;

public class RotatoriaReg extends Rotatoria {
	private Region r = new Region(0);

	@Override
	public int entra(final int settore) {
		r.enterWhen( new RegionCondition() {
			@Override
			public boolean evaluate() {
				return !occ[settore] && !occ[prec(settore)]; 
			}
		});

		occ[settore] = true;

		r.leave();
		return settore;
	}

	@Override
	public int prossimo(final int settore) {
		r.enterWhen( new RegionCondition() {			
			@Override
			public boolean evaluate() {
				return !occ[succ(settore)];
			}
		});
		
		occ[settore] = false;
		occ[succ(settore)] = true;
		
		r.leave();
		return succ(settore);
	}

	@Override
	public void esce(int settore) {
		r.enterWhen();
		
		occ[settore] = false;
		
		r.leave();
	}



}
