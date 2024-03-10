PROGRAM stexample

  VAR
    x3 : BOOL;
  END_VAR

/**/
  x5 := TRUE;
  
  x6 := 1 + 2 + 3;
  x7 := 4 + 5 - 6;


/**/
  REPEAT
    x1 := FALSE;
    x2 := FALSE;
  UNTIL x4 = FALSE
  //UNTIL TRUE
  END_REPEAT;


END_PROGRAM

/**/
CONFIGURATION Config0
  RESOURCE Res0 ON PLC
    TASK task0(INTERVAL := T#10s, PRIORITY := 0);
    PROGRAM instance0 WITH task0 : stexample;
  END_RESOURCE
END_CONFIGURATION


// https://www.plcacademy.com/structured-text-tutorial/