package exer.ucc02;

import os.MutexSem;
import os.Semaphore;
import os.Sys;

public class LavaggioAutoSem extends LavaggioAuto {
	
	private static final int MAXACC = 100;
	private MutexSem mutex = new MutexSem();
	private Semaphore codaParz = new Semaphore (0, MAXACC);
	private Semaphore codaTot = new Semaphore (0, MAXACC);
	
	
	  public static void main(String[] args) {
		  System.out.println(      "----- AUTO LAVAGGIO CON SEMAFORI PRIVATI");
		    int noC = Sys.in.readInt("--    inserire no. auto con lavaggio completo");
		    int noP = Sys.in.readInt("--    inserire no. auto con lavaggio parziale");
		    System.err.println(      "----- inserire Ctrl-C per terminare!");
		    LavaggioAuto al  = new LavaggioAutoSem(); 
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
	
	@Override
	public void prenotaParziale(String car) {
		mutex.p();
		
		codaParziale++;
		
		System.out.println("!! " + car + " prenota lavaggio parziale");
		stampaSituazione();
		
		if ((inEstParz + inEstTot) <= 8 && codaParziale == 1 && (codaTotale == 0 || (inEstTot + inInt) == 4)){
			
			inEstParz++;
			codaParziale--;
			
			codaParz.v();
		}
		mutex.v();
		codaParz.p();
		mutex.p();
		System.out.println("!! " + car + " accede al lavaggio esterno (parz)");
		stampaSituazione();
		mutex.v();
	}

	@Override
	public void pagaParziale(String car) {
		mutex.p();
		
		inEstParz--;
		
		System.out.println("!! " + car + " paga parziale");
		stampaSituazione();
		
		if ((inEstParz + inEstTot) <= 8){
			if (codaTotale != 0 && (inEstTot + inInt) != 4){
				
				inEstTot++;
				codaTotale--;
				
				codaTot.v();
			}
			else if (codaParziale != 0){
				
				inEstParz++;
				codaParziale--;
				
				codaParz.v(); 
			}
		}
		mutex.v();
	}

	@Override
	public void prenotaTotale(String car) {
		mutex.p();
		
		codaTotale++;
		
		System.out.println("!! " + car + " prenota lavaggio totale");
		stampaSituazione();
		
		if (codaTotale == 1 && (inEstTot + inInt) != 4 && (inEstTot + inEstParz) < 8){
			
			inEstTot++;
			codaTotale--;
			
			codaTot.v();
		}
		mutex.v();
		codaTot.p();
		mutex.p();
		System.out.println("!! " + car + " accede al lavaggio esterno (tot)");
		stampaSituazione();
		mutex.v();
	}

	@Override
	public void lavaInterno(String car) {
		mutex.p();
		
		inEstTot--;
		inInt++;
		
		System.out.println("!! " + car + " passa al lavaggio interno");
		stampaSituazione();
		mutex.v();
	}

	@Override
	public void pagaTotale(String car) {
		mutex.p();
		
		inInt--;
		
		System.out.println("!! " + car + " esce dal lavaggio interno");
		stampaSituazione();
		
		if ((inEstParz + inEstTot) <= 8){
			if (codaTotale != 0 && (inEstTot + inInt) != 4){
				inEstTot++;
				codaTotale--;
				codaTot.v();
			}
			else if (codaParziale != 0){
				inEstParz++;
				codaParziale--;
				codaParz.v();
			}
		}
		mutex.v();
	}
}
