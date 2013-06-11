with Ada.Text_IO; use Ada.Text_IO;
with Aeroporto;

procedure AeroMain is
	Commander: Aeroporto.Pilot;
	Pilots: Aeroporto.Squadron;
	N : string := "";

begin
  Put_Line ("Hello World - The AirPort is open");
  New_Line;
  Aeroporto.AC.registra(N);
  Put_Line ("Hello guys - I'm the Commander, AWK # " & N);
end AeroMain;