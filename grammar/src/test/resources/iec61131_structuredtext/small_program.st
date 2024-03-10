/*
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
*/

FUNCTION_BLOCK AKT_5_2_MV

  VAR_INPUT
    //status : S_FA_STATUS;
    Hand_P1 : BOOL;
    Hand_P2 : BOOL;
  END_VAR

/*
  VAR
    SR0 : SR;
  END_VAR
*/

  VAR_OUTPUT
    s_MV_P2_FF : BOOL;
  END_VAR

  VAR
    DebugBoolean : BOOL;
    TON0 : TON;
  END_VAR

/*
  VAR CONSTANT
    const_T_Laufzeit_P2 : TIME := T#10s;
  END_VAR
*/

  VAR_INPUT
    SEN_P1 : BOOL;
    SEN_P2 : BOOL;
  END_VAR

  VAR
    _TMP_AND6_OUT : BOOL;
    _TMP_AND9_OUT : BOOL;
    _TMP_NOT17_OUT : BOOL;
    _TMP_AND15_OUT : BOOL;
  END_VAR
/**/

/*
  _TMP_AND6_OUT := AND(status.BART_Hand, Hand_P2);
  _TMP_AND9_OUT := AND(status.BART_Hand, Hand_P1);
  SR0(S1 := _TMP_AND6_OUT, R := _TMP_AND9_OUT);
  s_MV_P2_FF := SR0.Q1;
  _TMP_NOT17_OUT := NOT(SEN_P2);
  _TMP_AND15_OUT := AND(_TMP_NOT17_OUT, Hand_P2);
  TON0(IN := _TMP_AND15_OUT, PT := const_T_Laufzeit_P2);
  DebugBoolean := TON0.Q;
*/
END_FUNCTION_BLOCK