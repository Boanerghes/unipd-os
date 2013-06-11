with Text_IO; use Text_IO;
package body Aeroporto is

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

procedure decolla(N:string) is   -- Decollo
begin
  TC.richAccessoPista;    Put_Line(N & ", Rolling Request");
  TC.richAutorizDecollo;  Put_Line(N & ", TakeOff Request");
  TC.inVolo(N);           Put_Line(N & ", On Air");
end decolla;

procedure atterra(N:string) is  -- Atterraggio
begin
  TC.inAvvicinamento;         Put_Line(N & ", Just for landing");
  TC.richAutorizAtterraggio;  Put_Line(N & ", Landing Request");  
  TC.freniAttivati;           Put_Line(N & ", Brakes On");
  TC.inParcheggio;            Put_Line(N & ", Parking");
end atterra;

procedure vola is               -- Passa il tempo
T: duration:=1.0;
begin
  delay T;
end vola;

task body AC is
  A: constant := 65;    C: constant := 30;
  M: constant := 127;
  V: Integer  := 1;     i: Integer := 0;

begin
  accept avvia do  end;
  loop
    select
      accept registra (N: OUT string) do 
         i:= i+1; N:= Integer'Image(i); end;
      or accept Rnd(X: OUT Integer) do 
        X:=V; V := (V*A + C) mod M; end;
    end select;
  end loop;
end AC;

task body Pilot is
N: string:="";

begin
  AC.registra(N);
  Put_Line ("Hello guys - I'm AWK # " & N);
 loop
    decolla(N); vola; atterra(N);
  end loop;
end Pilot;

end Aeroporto;