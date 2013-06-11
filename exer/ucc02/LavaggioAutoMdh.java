package exer.ucc02;

import os.Monitor;
import os.Sys;

public class LavaggioAutoMdh extends LavaggioAuto {

	  public static void main(String[] args) {
		  System.out.println(      "----- AUTO LAVAGGIO CON SEMAFORI PRIVATI");
		    int noC = Sys.in.readInt("--    inserire no. auto con lavaggio completo");
		    int noP = Sys.in.readInt("--    inserire no. auto con lavaggio parziale");
		    System.err.println(      "----- inserire Ctrl-C per terminare!");
		    LavaggioAuto al  = new LavaggioAutoMdh(); 
		    al.stampaSituazione();
		    VeicoloTotale veicoloCompleto[] = new VeicoloTotale[noC];
		    VeicoloParziale veicoloParziale[] = new VeicoloParziale[noP];
		    for(int i=1; i<=noC; i++)
		      veicoloCompleto[i-1] = new VeicoloTotale("C"+i, al);
		    for(int i=1; i<=noP; i++)
		      veicoloParziale[i-1] = new VeicoloParziale("P"+i, al);
		    for (int i = 0; i<noC; veicoloCompleto[i++].start()); 
		    for (int i = 0; i<noP; veicoloParziale[i++].start()); 
		  }//main	
	
	
	Monitor m = new Monitor();
	Monitor.Condition accEstTot = m.new Condition();
	Monitor.Condition accEstParz = m.new Condition();
	Monitor.Condition accInt = m.new Condition();

	@Override
	public void prenotaParziale(String car) {
		m.mEnter();

		codaParziale++;

		System.out.println("!! " + car + " prenota lavaggio parziale");
		stampaSituazione();

		if ((inEstParz + inEstTot) == 8 || codaParziale > 1 || (codaTotale != 0 && (inEstTot + inInt) != 4))
			accEstParz.cWait();

		inEstParz++;
		codaParziale--;

		System.out.println("!! " + car + " accede al lavaggio esterno (parz)");
		stampaSituazione();
		
		m.mExit();
	}

	@Override
	public void pagaParziale(String car) {
		m.mEnter();
		inEstParz--;
		
		System.out.println("!! " + car + " paga parziale");
		stampaSituazione();
		
		if (inEstTot + inEstParz < 8){
			if (codaTotale >= 1 && (inInt + inEstTot) < 4) {
				accEstTot.cSignal();
			}
			else if (codaParziale >= 1) {
				accEstParz.cSignal();
			}
		}
		m.mExit();
	}

	@Override
	public void prenotaTotale(String car) {
		m.mEnter();
		codaTotale++;
		
		System.out.println("!! " + car + " prenota lavaggio totale");
		stampaSituazione();
		
		if (codaTotale > 1 || (inInt + inEstTot) >= 4)
			accEstTot.cWait();
		
		codaTotale--;
		inEstTot++;
		
		System.out.println("!! " + car + " accede al lavaggio esterno (tot)");
		stampaSituazione();
		
		m.mExit();
	}

	@Override
	public void lavaInterno(String car) {
		m.mEnter();
		
		inEstTot--;
		inInt++;
		
		System.out.println("!! " + car + " passa al lavaggio interno");
		stampaSituazione();
		
		if (inEstTot + inEstParz < 8){
			if (codaTotale >= 1 && inEstTot + inInt < 4)
				accEstTot.cSignal();
			else if (codaParziale >= 1)
				accEstParz.cSignal();
		}
		
		m.mExit();
	}

	@Override
	public void pagaTotale(String car) {
		m.mEnter();
		
		inInt--;
		
		System.out.println("!! " + car + " esce dal lavaggio interno");
		stampaSituazione();
		
		if (inEstTot + inEstParz < 8){
			if (codaTotale >= 1 && (inInt + inEstTot) < 4)
				accEstTot.cSignal();
			else if (codaParziale >= 1)
				accEstParz.cSignal();
		}
		
		m.mExit();
	}
}
