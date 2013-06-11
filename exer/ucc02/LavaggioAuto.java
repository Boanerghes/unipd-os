package exer.ucc02;

public abstract class LavaggioAuto {

	protected int codaParziale = 0;
	protected int codaTotale = 0;
	protected int inEstParz = 0;
	protected int inEstTot = 0;
	protected int inInt = 0;
	
	public void stampaSituazione() {
		System.out.println("$ codaParz: " + codaParziale + ", codaTot: " + codaTotale + ", inEstParz: " + inEstParz + ", inEstTot: " + inEstTot + ", inInt: " + inInt);
	}
	
	public abstract void prenotaParziale (String car);
	public abstract void pagaParziale (String car);
	public abstract void prenotaTotale (String car);
	public abstract void lavaInterno (String car);
	public abstract void pagaTotale (String car);
}
