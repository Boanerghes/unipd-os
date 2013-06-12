package exer.ucc07;

public class RotatoriaMJ extends Rotatoria{
	
	@Override
	public synchronized int entra(int settore) {
		
		while(occ[settore] || occ[prec(settore)])
			try { wait(); }
			catch (InterruptedException e) {}
		
		occ[settore] = true;
		return settore;
	}

	@Override
	public synchronized int prossimo(int settore) {
		
		while (occ[succ(settore)])
			try { wait(); }
			catch (InterruptedException e) {}
		
		occ[succ(settore)] = true;
		occ[settore] = false;
		
		notifyAll();
		return succ(settore);
		
	}

	@Override
	public synchronized void esce(int settore) {
		occ[settore] = false;
		
		notifyAll();
	}
}
