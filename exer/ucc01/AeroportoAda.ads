package Aeroporto is

task AC is                        -- Comando aereo
  entry avvia;                    -- inizia la registrazione dei piloti
  entry registra(N: OUT string);  -- il pilota ottiene la sua matricola
  entry Rnd(X: OUT Integer);      -- numero casuale
end AC;

task TC is                        -- Torre di controllo
  entry richAccessoPista;         -- RAP il pilota richiede di impegnare la zona A
  entry richAutorizDecollo;       -- RAD il pilota impegna la zona B e libera la A
  entry inVolo(N: IN string);     -- IV  il pilota è in volo, la pista è libera
  entry inAvvicinamento;          -- IA  il pilota è in avvicinamento alla pista
  entry richAutorizAtterraggio;   -- RAA il pilota è in volo e deve atterrare
  entry freniAttivati;            -- FA  il pilota impegna la zona B e libera la A
  entry inParcheggio;             -- IP  il pilota esce dalla pista e libera B
end TC;

task type Pilot is  end Pilot;          -- Il pilota modello
type Squadron is array (1..5) of Pilot; -- La squadriglia

procedure decolla(N:string);
procedure atterra(N:string);
procedure vola;

end Aeroporto;
