package exer.ucc01;

import os.*;

/** {c}
  * TorreDiControlloSem.java
  * Problema Aeroporto -
  * Torre di controllo versione
  * con semafori privati
  * @version 1.00 2003-11-21
  * @author G.Clemente DEI UNIPD
  * @author M.Moro DEI UNIPD
  */

public class TorreDiControlloSem
  extends TorreDiControllo
{
    private static final int MAXACCA = 100;
    private static final int MAXATTAT = 100;
      // massimo accumulo
    private Semaphore accA = new Semaphore(0, MAXACCA);
      // privato accesso A
    private Semaphore accB = new Semaphore(0, 2);
      // privato accesso B
    private Semaphore attAt = new Semaphore(0, MAXATTAT);
      // privato attesa atterraggio
    private MutexSem mx = new MutexSem();
      // protezione accesso condiviso
      
    public void richAutorizAtterraggio(int io) 
    {
        mx.p();
        riAutAt++;
          // segnala la volont� di atterrare
        System.out.println(
          "$$ L'aereo A"+io+" !!!!!!!! RICHIESTA AUTORIZZAZIONE ATTERRAGGIO");
        stampaSituazioneAeroporto();
        if (occupateA+occupateB == 0)
        {
            // la pista e` completamente libera
            riAutAt--;
            occupateA=1;
            att=true;
            attAt.v();  // non attende
        }
        mx.v();
        attAt.p();  // eventuale attesa    
        // pista riservata
        mx.p();
        System.out.println(
          "$$$$ L'aereo A"+io+" IN FASE DI ATTERRAGGIO ");
        stampaSituazioneAeroporto();
        mx.v();
    }

    public void freniAttivati(int io) 
    {
        mx.p();
        occupateA=0;
        occupateB=1;
          // per ragioni di sicurezza,
          // pur passando in B
          // mantiene bloccata la pista
          // con att==true
        System.out.println(
          "$$$$$$ L'aereo A"+io+" TOCCA TERRA, FRENA ");
        stampaSituazioneAeroporto();
        mx.v();
    }

    public void inParcheggio(int io) 
    {
        mx.p();
        occupateB=0;
        att = false;
          // libera la pista
        contaAtt++;
        System.out.println(
          "$$$$$$$$ L'aereo A"+io+" LIBERA LA PISTA E PARCHEGGIA");
        stampaSituazioneAeroporto();
        if(riAutAt>=1)
        {
            // libera un altro che atterra
            riAutAt--;
            occupateA=1;
            att=true;
            attAt.v();
        }
        else if (riAccPi >= 2)
        {
            // nessun atterraggio in vista
            riAccPi-=2;
            occupateA+=2;
            accA.v();  // libera 2 per decollare
            accA.v();
        }
        else if (riAccPi >= 1)
        {
            // nessun atterraggio in vista
            // libera 1 per decollare
            riAccPi-=1;
            occupateA+=1;
            accA.v();
        }
        mx.v();
    }

    public void richAccessoPista(int io) 
    {
        mx.p();
        riAccPi++;
          // segnala la volont� di decollare
        System.out.println(
          "** L'aereo D"+io+" ^^^^^^^^ RICHIESTA PISTA PER DECOLLO");
        stampaSituazioneAeroporto();
        if (riAutAt == 0 && !att && occupateA <= 1)
        {
            // una zona A libera e nessun atterraggio in vista
            // non deve attendere, occupa zona A
            riAccPi--;
            occupateA++;
            accA.v();
        }
        mx.v();
        accA.p();  // eventuale attesa
        mx.p();
        System.out.println(
          "**** L'aereo D"+io+ " SI PREPARA AL DECOLLO");
        stampaSituazioneAeroporto();
        mx.v();
    }

    public void richAutorizDecollo(int io) 
    {
        mx.p();
        riAutDe++;
          // segnala la volont� di decollare
        System.out.println(
          "****** L'aereo D"+io+" RICHIEDE AUTORIZZAZIONE DECOLLO");
        stampaSituazioneAeroporto();
        // deve entrare in zona B
        if(occupateB <= 1)
        {
            // una zona B libera, non deve attendere
            // libera zona A e occupa zona B
            occupateA--;
            riAutDe--;
            occupateB++;
            accB.v();
            if (riAutAt==0 && riAccPi>=1)
            {
                // nessun atterraggio in vista
                // libera 1 per decollare 
                riAccPi--;
                occupateA++;
                accA.v();
                  // questo signal, a causa dell'uscita da
                  // e rientro in mutua esclusione
                  // potrebbe provocare un'inversione
                  // di stampe tra quella qui alla fine 
                  // del metodo e quella dell'areo
                  // in decollo che entra in zona A
            }
        }
        mx.v();
        accB.p();
        mx.p();
        System.out.println(
          "******** L'aereo D"+io+" IN FASE DI DECOLLO ");
        stampaSituazioneAeroporto();
        mx.v();
    }

    public void inVolo(int io) 
    {
        mx.p();
        occupateB--;
          // libera zona B
        contaDec++;
        System.out.println(
          "********** L'aereo D"+io+ " HA PRESO IL VOLO!!!!! ");
        stampaSituazioneAeroporto();
        if (riAutDe>=1)
        {
            // c'e` un decollo da autorizzare
            // libera zona A e occupa zona B
            occupateA--;
            riAutDe--;
            occupateB++;
            accB.v();
            if (riAutAt==0 && riAccPi>=1)
            {
                // nessun atterraggio in vista
                // libera 1 per decollare 
                riAccPi--;
                occupateA++;
                accA.v();
            }
        }
        else if (riAutAt>=1 && occupateA+occupateB==0)
        {
            // atterraggio in vista, pista libera
            // autorizza atterraggio
            riAutAt--;
            occupateA=1;
            att=true;
            attAt.v();
        }
        mx.v();
    }

} //{c} TorredDiControlloSem
