TYPE
  S_FA_STATUS : STRUCT
    BART_Hand : BOOL := TRUE;
    BART_Auto : BOOL;
    Stoerung : BOOL;
    Stoer_Quit : BOOL;
    NotHalt : BOOL;
    Takt : BOOL;
  END_STRUCT;
  Diag : STRUCT
    Stoerung : BOOL;
  END_STRUCT;
END_TYPE

FUNCTION_BLOCK AKT_5_2_MV
  VAR_IN_OUT
    status : S_FA_STATUS;
  END_VAR
  VAR_INPUT
    Hand_P1 : BOOL;
    Hand_P2 : BOOL;
    SEN_P1 : BOOL;
    SEN_P2 : BOOL;
  END_VAR
  VAR_OUTPUT
    s_MV_P2 : BOOL;
  END_VAR
  VAR
    s_MV_P2_FF : SR;
    F_LZ_P1 : BOOL;
    F_LZ_P2 : BOOL;
  END_VAR
  VAR_OUTPUT
    F_LZ : BOOL;
  END_VAR
  VAR
    RS0 : RS;
    TON0 : TON;
    const_LaufzeitFehler_T : TIME := T#10s;
    RS1 : RS;
    TON1 : TON;
    _TMP_AND6_OUT : BOOL;
    _TMP_AND1_OUT : BOOL;
    _TMP_OR10_OUT : BOOL;
    _TMP_NOT17_OUT : BOOL;
    _TMP_AND14_OUT : BOOL;
    _TMP_NOT25_OUT : BOOL;
    _TMP_AND23_OUT : BOOL;
    _TMP_OR28_OUT : BOOL;
  END_VAR

  _TMP_AND6_OUT := AND(status.BART_Hand, Hand_P2);
  _TMP_AND1_OUT := AND(status.BART_Hand, Hand_P1);
  _TMP_OR10_OUT := OR(_TMP_AND1_OUT, status.NotHalt);
  s_MV_P2_FF(S1 := _TMP_AND6_OUT, R := _TMP_OR10_OUT);
  s_MV_P2 := s_MV_P2_FF.Q1;
  _TMP_NOT17_OUT := NOT(SEN_P1);
  _TMP_AND14_OUT := AND(_TMP_NOT17_OUT, Hand_P1);
  TON0(IN := _TMP_AND14_OUT, PT := const_LaufzeitFehler_T);
  RS0(S := TON0.Q, R1 := status.Stoer_Quit);
  F_LZ_P1 := RS0.Q1;
  _TMP_NOT25_OUT := NOT(SEN_P2);
  _TMP_AND23_OUT := AND(_TMP_NOT25_OUT, Hand_P2);
  TON1(IN := _TMP_AND23_OUT, PT := const_LaufzeitFehler_T);
  RS1(S := TON1.Q, R1 := status.Stoer_Quit);
  F_LZ_P2 := RS1.Q1;
  _TMP_OR28_OUT := OR(F_LZ_P1, F_LZ_P2);
  F_LZ := _TMP_OR28_OUT;
END_FUNCTION_BLOCK

FUNCTION FA_SERV : BOOL
  VAR_IN_OUT
    status : S_FA_STATUS;
  END_VAR
  VAR_INPUT
    BART_Hand : BOOL;
    Stoer_Quit : BOOL;
  END_VAR

  status.BART_Hand := BART_Hand;
  status.Stoer_Quit := Stoer_Quit;
END_FUNCTION

PROGRAM program0
  VAR_EXTERNAL
    global_status : S_FA_STATUS;
  END_VAR
  VAR
    FA_BART_Hand_T_HMI : BOOL := TRUE;
    FA_Stoer_Quit_T_HMI : BOOL;
    Zyl1_T_P1_HMI : BOOL := TRUE;
    SEN_P1_T_HMI : BOOL := TRUE;
    Zyl1_T_P2_HMI : BOOL;
    SEN_P2_T_HMI : BOOL;
    AKT_5_2_MV0 : AKT_5_2_MV;
    s_MV_P2 : BOOL := FALSE;
    Diag : Diag;
    _TMP_FA_SERV1_OUT : BOOL;
  END_VAR

  AKT_5_2_MV0(status := global_status, Hand_P1 := Zyl1_T_P1_HMI, Hand_P2 := Zyl1_T_P2_HMI, SEN_P1 := SEN_P1_T_HMI, SEN_P2 := SEN_P2_T_HMI);
  s_MV_P2 := AKT_5_2_MV0.s_MV_P2;
  Diag.Stoerung := AKT_5_2_MV0.F_LZ;
  _TMP_FA_SERV1_OUT := FA_SERV(status := global_status, BART_Hand := FA_BART_Hand_T_HMI, Stoer_Quit := FA_Stoer_Quit_T_HMI);
END_PROGRAM


CONFIGURATION Config0

  RESOURCE Res0 ON PLC
    VAR_GLOBAL
      global_status : S_FA_STATUS;
    END_VAR
    TASK task0(INTERVAL := T#3s,PRIORITY := 0);
    PROGRAM instance0 WITH task0 : program0;
  END_RESOURCE
END_CONFIGURATION
