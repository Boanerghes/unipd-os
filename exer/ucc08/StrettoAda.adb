procedure Stretto is
	task CS is
		entry entraAB;
		entry entraBA;
		entry esceAB;
		entry esceBA;
	end CS;

	task body CS is

	totBarche := 0;
	passing := 0;
	side := 'N';

	begin
		loop
			select
			accept esceAB do
				totBarche := totBarche + 1;
				if entraAB'count = 0 or entraBA'count =0 then
					side := 'N';
					passing := 0;
				end if;
			end;
			or accept esceBA do
				totBarche := totBarche + 1;
				if entraAB'count = 0 or entraBA'count =0 then
					side := 'N';
					passing := 0;
				end if;
			end;
			or when side = 'N' or side = 'A' and passing < 2 => accept entraAB do
				if entraAB'count = 0 or entraBA'count = 1 then
					side := 'B';
					passed := 0;
				elsif (side = 'N') then
						side := 'A';
						passing = 1;
				elsif (side = 'A') then
						passed := passed + 1;
						if passed = 2 then
							side := 'B';
							passed := 0;
						end if;
					end if;
				end if;
			end;
			or when side = 'N' or side = 'B' => accept entraBA and totIn < 2 do
				if entraAB'count > 1 and entraBA'count > 1 then
					if (side = 'N') then
						side := 'B';
						passed = 1;
					elsif (side = 'B') then
						passed := passed + 1;
						if passed = 2 then
							side := 'A';
							passed := 0;
						end if;
					end if;
				elsif entraBA'count = 0 or entraAB'count = 1 then
					side := 'A';
					passed := 0;
				end if;
			end;
			end select;
		end loop;
	end CS;