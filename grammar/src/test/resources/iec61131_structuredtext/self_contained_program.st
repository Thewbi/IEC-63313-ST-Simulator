PROGRAM stexample

  VAR
    x : BOOL;
  END_VAR

  x := TRUE;

  REPEAT
    x := FALSE;
  UNTIL x = FALSE
  END_REPEAT;

END_PROGRAM

CONFIGURATION Config0
  RESOURCE Res0 ON PLC
    TASK task0(INTERVAL := T#10s, PRIORITY := 0);
    PROGRAM instance0 WITH task0 : stexample;
  END_RESOURCE
END_CONFIGURATION

// https://www.plcacademy.com/structured-text-tutorial/