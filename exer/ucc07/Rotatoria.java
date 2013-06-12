package exer.ucc07;

public abstract class Rotatoria {
	protected boolean[] occ = {false, false, false, false};
	public static final int SETTORI = 4;
	
	public abstract int entra (int settore);
	public abstract int prossimo (int settore);
	public abstract void esce (int settore);
	
	protected int succ (int sett) {
		return (sett + 1) % SETTORI;
	}
	
	protected int prec (int sett) {
		return (sett - 1 + SETTORI) % SETTORI;
	}
	
}
