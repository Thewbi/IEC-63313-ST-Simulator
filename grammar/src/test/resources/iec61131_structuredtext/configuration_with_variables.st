TYPE
  S_FA_STATUS : STRUCT
    BART_Hand : BOOL := TRUE;
    BART_Auto : BOOL := FALSE;
    Stoerung : BOOL := FALSE;
    Stoer_Quit : BOOL := FALSE;
    NotHalt : BOOL := FALSE;
    Takt : BOOL := FALSE;
  END_STRUCT;
END_TYPE

CONFIGURATION Config0

  RESOURCE Res0 ON PLC
    VAR_GLOBAL
      global_status : S_FA_STATUS;
      //global_status : BOOL;
    END_VAR
    TASK task0(INTERVAL := T#20ms,PRIORITY := 0);
    PROGRAM instance0 WITH task0 : program0_POU;
  END_RESOURCE
  
END_CONFIGURATION