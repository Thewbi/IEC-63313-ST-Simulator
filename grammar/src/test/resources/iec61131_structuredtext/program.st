PROGRAM M_AIN_2

VAR
END_VAR

//PROGRAM main
/*
VAR
	test: BOOL := TRUE;
	test2: INT := 10;
END_VAR

test := TRUE;
test2 := 14;
*/

(*
VAR
	test: BOOL := TRUE;
	test2: INT := 10;
END_VAR

test := TRUE;
test2 := 14;
*)


VAR
	test: BOOL := FALSE;
	test2: DINT := -12;
	test3: DINT := 0;
END_VAR

test := TRUE;
test2 := -12;
test2 := 4*14;

FOR test2 := 1 TO 10 DO
	test3 := 1;
	//test3 := test3 + 1;
END_FOR;

IF A = 0 THEN
	SEL := 1;
END_IF;

IF G = 0 THEN
	SEL := 1;
ELSE
	SEL := 2;
END_IF;

CASE K OF
 0: MUX := 1;
 1: MUX := 2;
 2: MUX := 3;
END_CASE;

CASE K OF
 0: MUX := 1;
 1: MUX := 2;
 2: MUX := 3;
 ELSE MUX := 4;
END_CASE;

END_PROGRAM