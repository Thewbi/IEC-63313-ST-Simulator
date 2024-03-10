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