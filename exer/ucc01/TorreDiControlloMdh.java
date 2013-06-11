package exer.ucc01;

import os.*;

/** {c}
  * TorreDiControlloMdh.java
  * Problema Aeroporto -
  * Torre di controllo versione
  * con Monitor di Hoare
  * @version 1.00 2003-11-22
  * @author G.Clemente DEI UNIPD
  * @author M.Moro DEI UNIPD
  */

public class TorreDiControlloMdh
  extends TorreDiControllo
{
    // non potendo estendere anche Monitor,
    // ne viene dichiarata una variabile
    // e i necessari Condition
    private Monitor tower = new Monitor();
    private Monitor.Condition accA = tower.new Condition();
      // condition privato accesso A
    private Monitor.Condition accB = tower.new Condition();
      // condition privato accesso B
    private Monitor.Condition attAt = tower.new Condition();
      // condition privato attesa atterraggio
    
      
    public void richAutorizAtterraggio(int io) 
    {
        tower.mEnter();
        riAutAt++;
          // segnala la volontà di atterrare
        System.out.println(
          "$$ L'aereo A"+io+" !!!!!!!! RICHIESTA AUTORIZZAZIONE ATTERRAGGIO");
        stampaSituazioneAeroporto();
        if (occupateA+occupateB != 0)
            // pista occupata, attende
            attAt.cWait();
        // la pista e` completamente libera
        riAutAt--;
        occupateA=1;
        att=true;
        // pista riservata
        System.out.println(
          "$$$$ L'aereo A"+io+" IN FASE DI ATTERRAGGIO ");
        stampaSituazioneAeroporto();
        tower.mExit();
    }

    public void freniAttivati(int io) 
    {
        tower.mEnter();
        occupateA=0;
        occupateB=1;
          // per ragioni di sicurezza,
          // pur passando in B
          // mantiene bloccata la pista
          // con att==true
        System.out.println(
          "$$$$$$ L'aereo A"+io+" TOCCA TERRA, FRENA ");
        stampaSituazioneAeroporto();
        tower.mExit();
    }

    public void inParcheggio(int io) 
    {
        tower.mEnter();
        occupateB=0;
        att = false;
          // libera la pista
        contaAtt++;
        System.out.println(
          "$$$$$$$$ L'aereo A"+io+" LIBERA LA PISTA E PARCHEGGIA");
        stampaSituazioneAeroporto();
        if(riAutAt>=1)
            // libera un altro che atterra
            attAt.cSignal();
        else if (riAccPi >= 2)
        {
            // nessun atterraggio in vista
            // libera 2 per decollare
            accA.cSignal();
            accA.cSignal();
        }
        else if (riAccPi >= 1)
            // nessun atterraggio in vista
            // libera 1 per decollare
            accA.cSignal();
        tower.mExit();
    }

    public void richAccessoPista(int io) 
    {
        tower.mEnter();
        riAccPi++;
          // segnala la volontà di decollare
        System.out.println(
          "** L'aereo D"+io+" ^^^^^^^^ RICHIESTA PISTA PER DECOLLO");
        stampaSituazioneAeroporto();
        if (riAutAt != 0 || att || occupateA >= 2) 
            // pista occupata o atterraggio in vista
            // deve attendere
            accA.cWait();
        // una zona A libera, la occupa
        riAccPi--;
        occupateA++;
        System.out.println(
          "**** L'aereo D"+io+ " SI PREPARA AL DECOLLO");
        stampaSituazioneAeroporto();
        tower.mExit();
    }

    public void richAutorizDecollo(int io) 
    {
        tower.mEnter();
        riAutDe++;
          // segnala la volontà di decollare
        System.out.println(
          "****** L'aereo D"+io+" RICHIEDE AUTORIZZAZIONE DECOLLO");
        stampaSituazioneAeroporto();
        // deve entrare in zona B
        if(occupateB >= 2)
            // zona B occupata, deve attendere
            accB.cWait();
        // una zona B libera
        // libera zona A e occupa zona B
        occupateA--;
        riAutDe--;
        occupateB++;
        System.out.println(
          "******** L'aereo D"+io+" IN FASE DI DECOLLO ");
        stampaSituazioneAeroporto();
        if (riAutAt==0 && riAccPi>=1)
            // nessun atterraggio in vista
            // libera 1 per decollare 
            accA.cSignal();
        tower.mExit();
    }

    public void inVolo(int io) 
    {
        tower.mEnter();
        occupateB--;
          // libera zona B
        contaDec++;
        System.out.println(
          "********** L'aereo D"+io+ " HA PRESO IL VOLO!!!!! ");
        stampaSituazioneAeroporto();
        if (riAutDe>=1)
            // c'e` un decollo da autorizzare
            // libera zona A e occupa zona B
            accB.cSignal();
        else if (riAutAt>=1 && occupateA+occupateB==0)
            // atterraggio in vista, pista libera
            // autorizza atterraggio
            attAt.cSignal();
        tower.mExit();
    }

} //{c} TorredDiControlloMdh
