package exer.ucc08;

public abstract class Stretto {
	// totale barche passate nello stretto
	protected int totBarche = 0;
	// barche che stanno attraversando
	protected int passing = 0;
	// lato del senso unico alternato
	protected Side side = Side.N;
	// numero di barche passate in questo senso unico (mai oltre 1)
	protected int sCounter = 0;
	// definisce le direzioni possibili del senso unico alternato
	protected enum Side {
		A, B, N
	}

	public abstract void entraAB(int io); 
	public abstract void entraBA(int io); 
	public abstract void esceAB(int io);
	public abstract void esceBA(int io);
}
