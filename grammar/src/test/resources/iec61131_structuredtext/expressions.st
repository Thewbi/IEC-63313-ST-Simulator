PROGRAM expressions

VAR
END_VAR

test := TRUE;

// IF (Tc2_Standard.LEN(STR := MsgFmtString) + LEN(STR := StringArg)) <= MSG_FMT_STRING_MAX_NUMBER_OF_CHARACTERS THEN

IF ((Tc2_Standard.LEN(STR := MsgFmtString) + LEN(STR := StringArg)) <= MSG_FMT_STRING_MAX_NUMBER_OF_CHARACTERS) THEN
	SEL := 1;
END_IF;

(*
IF 1 THEN
	SEL := 1;
END_IF;

IF (1) THEN
	SEL := 1;
END_IF;

IF 1 + (1) THEN
	SEL := 1;
END_IF;

IF (1) + (1) THEN
	SEL := 1;
END_IF;

IF A < 0 THEN
	SEL := 1;
END_IF;

IF A + 1 < 0 THEN
	SEL := 1;
END_IF;

IF A < 0 + 1 THEN
	SEL := 1;
END_IF;

IF A + 1 < 0 + 1 THEN
	SEL := 1;
END_IF;

IF A + 1 + 2 < 0 + 1 + 2 THEN
	SEL := 1;
END_IF;

IF A > 0 THEN
	SEL := 1;
END_IF;

IF A + 1 > 0 THEN
	SEL := 1;
END_IF;

IF A > 0 + 1 THEN
	SEL := 1;
END_IF;

IF A + 1 > 0 + 1 THEN
	SEL := 1;
END_IF;

IF A <= 0 THEN
	SEL := 1;
END_IF;

IF A + 1 <= 0 THEN
	SEL := 1;
END_IF;

IF A <= 0 + 1 THEN
	SEL := 1;
END_IF;

IF A + 1 <= 0 + 1 THEN
	SEL := 1;
END_IF;

IF A >= 0 THEN
	SEL := 1;
END_IF;

IF A + 1 >= 0 THEN
	SEL := 1;
END_IF;

IF A >= 0 + 1 THEN
	SEL := 1;
END_IF;

IF A + 1 >= 0 + 1 THEN
	SEL := 1;
END_IF;

IF (A < 0) THEN
	SEL := 1;
END_IF;

IF (A + 1 < 0) THEN
	SEL := 1;
END_IF;

IF (A < 0 + 1) THEN
	SEL := 1;
END_IF;

IF (A + 1 < 0 + 1) THEN
	SEL := 1;
END_IF;

IF (A > 0) THEN
	SEL := 1;
END_IF;

IF (A + 1 > 0) THEN
	SEL := 1;
END_IF;

IF (A > 0 + 1) THEN
	SEL := 1;
END_IF;

IF (A + 1 > 0 + 1) THEN
	SEL := 1;
END_IF;

IF (A <= 0) THEN
	SEL := 1;
END_IF;

IF (A + 1 <= 0) THEN
	SEL := 1;
END_IF;

IF (A <= 0 + 1) THEN
	SEL := 1;
END_IF;

IF (A + 1 <= 0 + 1) THEN
	SEL := 1;
END_IF;

IF (A >= 0) THEN
	SEL := 1;
END_IF;

IF (A + 1 >= 0) THEN
	SEL := 1;
END_IF;

IF (A >= 0 + 1) THEN
	SEL := 1;
END_IF;

IF (A + 1 >= 0 + 1) THEN
	SEL := 1;
END_IF;

IF (A + 1 + 2 >= 0 + 1 + 2) THEN
	SEL := 1;
END_IF;

IF (A.B >= 1) THEN
	SEL := 1;
END_IF;

IF (1 >= C.D) THEN
	SEL := 1;
END_IF;

IF (A.B >= C.D) THEN
	SEL := 1;
END_IF;
*)

END_PROGRAM