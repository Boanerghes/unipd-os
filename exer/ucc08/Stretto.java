package exer.ucc08;

public abstract class Stretto {
	protected int totBarche = 0;
	protected int passing = 0;
	protected Side side = Side.N;
	protected int sCounter = 0;
	protected enum Side {
		A, B, N
	}

	public abstract void entraAB(int io); 
	public abstract void entraBA(int io); 
	public abstract void esceAB(int io);
	public abstract void esceBA(int io);
}
