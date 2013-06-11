package exer.ucc01;

import os.*;

/** {c}
  * TorreDiControlloMdj.java
  * Problema Aeroporto -
  * Torre di controllo versione
  * Monitor di Java
  * @version 1.00 2001-03-28
  * @version 2.00 2003-11-21
  * @author S.Cecchin Ist. Negrelli Feltre
  * @author M.Moro DEI UNIPD
  */

public class TorreDiControlloMdj 
  extends TorreDiControllo
{

    public synchronized void richAutorizAtterraggio(int io) 
    {
        riAutAt++;
          // segnala la volontà di atterrare,
        System.out.println(
          "$$ L'aereo A"+io+" !!!!!!!! RICHIESTA AUTORIZZAZIONE ATTERRAGGIO");
        stampaSituazioneAeroporto();
        while(occupateA+occupateB != 0)
            // le piste non sono completamente libere
            try {
                wait();
            } catch(InterruptedException e) {}
        // blocca pista 
        riAutAt--;
        occupateA=1;
        att=true;
        System.out.println(
          "$$$$ L'aereo A"+io+" IN FASE DI ATTERRAGGIO ");
        stampaSituazioneAeroporto();
    }

    public synchronized void freniAttivati(int io) 
    {
        occupateA=0;
        occupateB=1;
          // per ragioni di sicurezza,
          // pur passando in B
          // mantiene bloccata la pista
          // con att==true
        System.out.println(
          "$$$$$$ L'aereo A"+io+" TOCCA TERRA, FRENA ");
        stampaSituazioneAeroporto();
    }

    public synchronized void inParcheggio(int io) 
    {
        occupateB=0;
        att = false;
        contaAtt++;
        System.out.println(
          "$$$$$$$$ L'aereo A"+io+" LIBERA LA PISTA E PARCHEGGIA");
        stampaSituazioneAeroporto();
        notifyAll();
    }

    public synchronized void richAccessoPista(int io) 
    {
        riAccPi++;
          // segnala la volontà di decollare
        System.out.println(
          "** L'aereo D"+io+" ^^^^^^^^ RICHIESTA PISTA PER DECOLLO");
        stampaSituazioneAeroporto();
        while(riAutAt > 0 || att || occupateA == 2) 
        {
            // attende gli atterraggi che sono prioritari
            // e che si liberi la zona A
            try {
                wait();
            } catch(InterruptedException e) {}
        }
        riAccPi--;
        occupateA++;;
          // occupa zona A
        System.out.println(
          "**** L'aereo D"+io+ " SI PREPARA AL DECOLLO");
        stampaSituazioneAeroporto();
    }

    public synchronized void richAutorizDecollo(int io) 
    {
        riAutDe++;
          // segnala la volontà di decollare
        System.out.println(
          "****** L'aereo D"+io+" RICHIEDE AUTORIZZAZIONE DECOLLO");
        stampaSituazioneAeroporto();
        // deve entrare in zona B
        while(occupateB == 2)
            try {
                wait();
            } catch(InterruptedException e) {}
        riAutDe--;
        occupateA--;
        occupateB++;
          // libera zona A e occupa zona B
        System.out.println(
          "******** L'aereo D"+io+" IN FASE DI DECOLLO ");
        stampaSituazioneAeroporto();
        notifyAll();
    }

    public synchronized void inVolo(int io) 
    {
        occupateB--;
          // libera zona B
        contaDec++;
        System.out.println(
          "********** L'aereo D"+io+ " HA PRESO IL VOLO!!!!! ");
        stampaSituazioneAeroporto();
        notifyAll();
    }

} //{c} TorredDiControlloMdj
