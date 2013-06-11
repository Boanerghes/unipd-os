package exer.ucc01;

import os.*;

/** {c}
  * TorreDiControlloReg.java
  * Problema Aeroporto -
  * Torre di controllo con Regioni critiche
  * @version 1.00 2001-03-28
  * @version 2.00 2003-11-22
  * @author S.Cecchin Ist. Negrelli Feltre
  * @author M.Moro DEI UNIPD
  */

public class TorreDiControlloReg extends TorreDiControllo {
  // non potendo estendere anche Region, ne dichiara una variabile
  private Region tower = new Region(0);

  public void richAutorizAtterraggio(int io) {
    tower.enterWhen();
    riAutAt++;						// segnala la volontà di atterrare,
    System.out.println("$$ L'aereo A"+io+
      " !!!!!!!! RICHIESTA AUTORIZZAZIONE ATTERRAGGIO");
    stampaSituazioneAeroporto();
    tower.leave();
    tower.enterWhen(new RegionCondition() {
      // classe anonima di valutazione
      public boolean evaluate()
      { return occupateA+occupateB==0; }
    }); // {c} <anonim>
    // blocca pista 
    riAutAt--;
    occupateA=1; att=true;          // la pista ora è riservata
    System.out.println("$$$$ L'aereo A"+io+" IN FASE DI ATTERRAGGIO ");
    stampaSituazioneAeroporto();
    tower.leave();
  }

  public void freniAttivati(int io) {
    tower.enterWhen();
    occupateA=0; occupateB=1;
    // per ragioni di sicurezza, pur passando in B
    // mantiene bloccata la pista con att==true
    System.out.println("$$$$$$ L'aereo A"+io+" TOCCA TERRA, FRENA ");
    stampaSituazioneAeroporto();
    tower.leave();
  }

  public void inParcheggio(int io) {
    tower.enterWhen();
    occupateB=0; att = false; 		// libera la pista
    contaAtt++;
    System.out.println("$$$$$$$$ L'aereo A"+io+" LIBERA LA PISTA E PARCHEGGIA");
    stampaSituazioneAeroporto();
    tower.leave();
  }

  public void richAccessoPista(int io) {
    tower.enterWhen();
    riAccPi++; 						// segnala la volontà di decollare
    System.out.println("** L'aereo D"+io+" ^^^^^^^^ RICHIESTA PISTA PER DECOLLO");
    stampaSituazioneAeroporto();
    tower.leave();
    tower.enterWhen(new RegionCondition() {
      public boolean evaluate()	// nessun atterraggio in vista e pista libera
      { return riAutAt == 0 && !att && occupateA <= 1; }
    }); // {c} <anonim>
    riAccPi--; occupateA++;			// una zona A libera, la occupa
    System.out.println("**** L'aereo D"+io+ " SI PREPARA AL DECOLLO");
    stampaSituazioneAeroporto();
    tower.leave();
  }

  public void richAutorizDecollo(int io) {
    tower.enterWhen();
    riAutDe++; 						// segnala la volontà di decollare
    System.out.println("****** L'aereo D"+io+" RICHIEDE AUTORIZZAZIONE DECOLLO");
    stampaSituazioneAeroporto();
    tower.leave();
    // deve entrare in zona B
    tower.enterWhen(new RegionCondition(){
      // classe anonima di valutazione
      public boolean evaluate()
      { return occupateB <= 1; }
    }); // {c} <anonim>
    // una zona B libera libera zona A e occupa zona B
    occupateA--; occupateB++;
    riAutDe--;
    System.out.println("******** L'aereo D"+io+" IN FASE DI DECOLLO ");
    stampaSituazioneAeroporto();
    tower.leave();
  }

  public void inVolo(int io) {
    tower.enterWhen();
    occupateB--; 					// libera zona B
    contaDec++;
    System.out.println("********** L'aereo D"+io+ " HA PRESO IL VOLO!!!!! ");
    stampaSituazioneAeroporto();
    tower.leave();
  }

} //{c} TorreDiControlloReg
