with Ada.Text_IO; use Ada.Text_IO;

procedure Aeroporto IS

  task AC is                        -- Comando aereo
    entry avvia;                    -- inizia la registrazione dei piloti
    entry registra(N: OUT Integer); -- il pilota ottiene la sua matricola
    entry Rnd(X: OUT Integer);      -- numero casuale
  end AC;

  task body AC is
    A: constant := 65;    C: constant := 30;    M: constant := 127;
    V: Integer  := 1;     i: Integer := 0;
  begin
    accept avvia do
      Put_Line ("Hello World - The AirPort is open");
      New_Line;
    end;
    loop
      select
        accept registra (N: OUT Integer) do		-- generatore di numeri progressivi
           i:= i+1;  N:= I;j
        end;
        or accept Rnd(X: OUT Integer) do			-- generatore pseudo casuale
          X:=V; V := (V*A + C) mod M; end;
      end select;
    end loop;
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

  task body TC is
  occupateA, occupateB: integer:=0;
  c, inArrivo:integer:=0;
  inAtterraggio:boolean:=false;
  begin
    loop
      select
      when not inAtterraggio and inArrivo=0 and occupateA<2 =>
        accept richAccessoPista do
          occupateA:=occupateA+1; end;
      or when occupateB<2 =>
        accept richAutorizDecollo do
          occupateA:=occupateA-1;
          occupateB:=occupateB+1; end;
      or accept inVolo(N: IN string) do
          occupateB:=occupateB-1; c:=c+1;
          Put_Line("Bye " & N); end;
      or accept inAvvicinamento do
          inArrivo := inArrivo+1; end;
      or when occupateA+occupateB=0 =>
        accept richAutorizAtterraggio do
          inArrivo:= inArrivo-1;
          inAtterraggio:= true;
          occupateA:=occupateA+1; end;
      or accept freniAttivati do
          occupateA:=occupateA-1;
          occupateB:=occupateB+1; end;
      or accept inParcheggio do
          inAtterraggio:= false;
          occupateB:=0;  c:=c-1; end;
      end select;
    end loop;
  end TC;

  task type Pilot;

  task body Pilot is
  I: integer; 
  N: string := Integer'Image(0);

  procedure decolla(N:IN string) is   -- Decollo
  begin
    TC.richAccessoPista;    Put_Line(N & ", ok Rolling Request");
    TC.richAutorizDecollo;  Put_Line(N & ", ok TakeOff Request");
    TC.inVolo(N);           Put_Line(N & ", On Air");
  end decolla;

  procedure atterra(N:IN string) is  -- Atterraggio
  begin
    TC.inAvvicinamento;         Put_Line(N & ", Just for landing");
    TC.richAutorizAtterraggio;  Put_Line(N & ", ok Landing Request");
    TC.freniAttivati;           Put_Line(N & ", Brakes On");
    TC.inParcheggio;            Put_Line(N & ", Parking");
  end atterra;

  procedure vola is               -- Passa il tempo
  T: duration:=5.0;
  begin
    delay T;
  end vola;

  begin
    AC.registra(I); N := Integer'Image(I);
    Put_Line ("Hello guys - I'm AWK # " & N);
    loop
      decolla(N); vola; atterra(N);
    end loop;
  end Pilot;
  
  Commander : Pilot;
  type Squadron is array (1..5) of Pilot; -- La squadriglia
  Pilots: Squadron;

begin                                           
  AC.avvia;
end Aeroporto;