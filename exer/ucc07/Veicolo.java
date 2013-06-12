package exer.ucc07;

import os.Util;

public class Veicolo extends Thread{
	private Rotatoria rot;
	private int id, settore;
	
	public Veicolo (Rotatoria rot, int veicolo, int settore) {
		this.rot = rot;
		this.id = veicolo;
		this.settore = settore;
	}
	
	@Override
	public void run() { 
		System.out.println("*1* Veicolo "+ id +" tenta di entrare nel settore "+ settore);
		settore = rot.entra(settore);
		System.out.println("*2* Veicolo "+ id +" entrato nel settore "+ settore);
		int set = Util.randVal(1, 8);
		for (int i=0; i<set; i++) {
			 Util.rsleep(500, 4000);
			System.out.println("*3* Veicolo "+ id +" in "+ settore + " tenta di entrare nel successivo");
			settore = rot.prossimo(settore);
			System.out.println("*4* Veicolo "+ id +" passato al settore " + settore);

		}
		System.out.println("*5* Veicolo "+ id +" tenta di uscire dal settore " + settore);
		rot.esce(settore);
		System.out.println("6* Veicolo "+ id +" uscito dal settore " + settore);

	}
	
	public static void main(String args[])
    {
        final int MAXVEICOLI = 10;
        
        RotatoriaMJ rt = new RotatoriaMJ();
        for (int i=1; i<= MAXVEICOLI; i++)
        {
            Veicolo v = new Veicolo(rt, i, Util.randVal(0, 3));
            v.start();
            Util.rsleep(500, 4000);
        }
    } //[m][s] main
}
