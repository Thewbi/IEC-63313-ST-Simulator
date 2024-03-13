TYPE
  S_FA_STATUS : STRUCT
    BART_Hand : BOOL := TRUE;
    BART_Auto : BOOL := FALSE;
    Stoerung : BOOL := FALSE;
    Stoer_Quit : BOOL := FALSE;
    NotHalt : BOOL := FALSE;
    Takt : BOOL := FALSE;
  END_STRUCT;
  Diag : STRUCT
    F_Paar_EN : BOOL := TRUE;
    F_LZ_P1_EN : BOOL := TRUE;
    F_LZ_P2_EN : BOOL := TRUE;
    F_LZ_P1_Zeit : TIME;
    F_LZ_P2_Zeit : TIME;
    Stoerung : BOOL := FALSE;
  END_STRUCT;
END_TYPE

FUNCTION_BLOCK AKT_5_2_MV
  VAR_IN_OUT
    status : S_FA_STATUS;
  END_VAR
  VAR_INPUT
    Hand_P1 : BOOL;
    Hand_P2 : BOOL;
  END_VAR
  VAR
    SR0 : SR;
  END_VAR
  VAR_OUTPUT
    s_MV_P2_FF : BOOL;
  END_VAR
  VAR
    DebugBoolean : BOOL;
    TON0 : TON;
  END_VAR
  VAR CONSTANT
    const_T_Laufzeit_P2 : TIME := T#10s;
  END_VAR
  VAR_INPUT
    SEN_P1 : BOOL;
    SEN_P2 : BOOL;
  END_VAR
  VAR
    RS0 : RS;
  END_VAR
  VAR_INPUT
    F_LZ_P2 : RS;
  END_VAR
  VAR_OUTPUT
    F_LZ_P3 : BOOL;
  END_VAR
  VAR_IN_OUT
    Diag : Diag;
  END_VAR
  VAR
    _TMP_AND6_OUT : BOOL;
    _TMP_AND9_OUT : BOOL;
    _TMP_NOT17_OUT : BOOL;
    _TMP_AND15_OUT : BOOL;
  END_VAR

  _TMP_AND6_OUT := AND(status.BART_Hand, Hand_P2);
  _TMP_AND9_OUT := AND(status.BART_Hand, Hand_P1);
  SR0(S1 := _TMP_AND6_OUT, R := _TMP_AND9_OUT);
  s_MV_P2_FF := SR0.Q1;
  _TMP_NOT17_OUT := NOT(SEN_P2);
  _TMP_AND15_OUT := AND(_TMP_NOT17_OUT, Hand_P2);
  TON0(IN := _TMP_AND15_OUT, PT := const_T_Laufzeit_P2);
  RS0(S := TON0.Q);
  F_LZ_P3 := RS0.Q1;
  Diag.Stoerung := F_LZ_P3;
END_FUNCTION_BLOCK
/*
FUNCTION FA_SERV : BOOL
  VAR_INPUT
    status : S_FA_STATUS;
    BART_Hand : BOOL;
    BART_Auto : BOOL;
    Stoerung : BOOL;
    Stoer_Quit : BOOL;
    NotHalt : BOOL;
  END_VAR

  status.BART_Hand := BART_Hand;
  FA_SERV := status.BART_Hand;
  status.BART_Auto := BART_Auto;
  status.Stoerung := Stoerung;
  status.Stoer_Quit := Stoer_Quit;
  status.NotHalt := NotHalt;
END_FUNCTION
*/
PROGRAM program0_POU
  VAR
    FA_Start_T_HMI : BOOL;
    FA_Init_T_HMI : BOOL;
    FA_Sonder_T_HMI : BOOL;
    FA_Stop_T_HMI : BOOL;
    FA_BART_Hand_T_HMI : BOOL := TRUE;
    FA_BART_Auto_T_HMI : BOOL;
    FA_BART_Auto_Einzel_T_HMI : BOOL;
    FA_Stoerung_T_HMI : BOOL;
    FA_Stoer_Quit_T_HMI : BOOL;
    FA_NotHalt_T_HMI : BOOL;
    DebugBoolean : BOOL;
  END_VAR
  VAR_EXTERNAL
    global_status : S_FA_STATUS;
  END_VAR
  VAR
    AKT_5_2_MV0 : AKT_5_2_MV;
    Zyl1_T_P1_HMI : BOOL;
    Zyl1_T_P2_HMI : BOOL;
    s_MV_P2_FF : BOOL;
    SEN_P1_T_HMI : BOOL := TRUE;
    SEN_P2_T_HMI : BOOL := FALSE;
    Diag : Diag;
    _TMP_FA_SERV1_OUT : BOOL;
  END_VAR

  global_status.BART_Hand := FA_BART_Hand_T_HMI;
  _TMP_FA_SERV1_OUT := FA_SERV(global_status, FA_BART_Hand_T_HMI, FA_BART_Auto_T_HMI, FA_Stoerung_T_HMI, FA_Stoer_Quit_T_HMI, FA_NotHalt_T_HMI);
  DebugBoolean := _TMP_FA_SERV1_OUT;
  AKT_5_2_MV0(status := global_status, Hand_P1 := Zyl1_T_P1_HMI, Hand_P2 := Zyl1_T_P2_HMI, SEN_P1 := SEN_P1_T_HMI, SEN_P2 := SEN_P2_T_HMI, Diag := Diag);
  s_MV_P2_FF := AKT_5_2_MV0.s_MV_P2_FF;
END_PROGRAM

/*
FUNCTION_BLOCK FA
  VAR
    Status : S_FA_STATUS;
  END_VAR

  Status.Stoerung := TRUE;
END_FUNCTION_BLOCK

FUNCTION_BLOCK SequenctialFunctionChart
  VAR
    RedLight : BOOL;
    OrangeLight : BOOL;
    GreenLight : BOOL;
    Step_0_Active : BOOL := TRUE;
    Step_1_Active : BOOL := FALSE;
    Step_2_Active : BOOL := FALSE;
    TON0 : TON;
    TON1 : TON;
    TON2 : TON;
  END_VAR

  INITIAL_STEP Step0:
    RedLight(N);
    Step_0_Active(N);
    COMPUTE_FUNCTION_BLOCKS(S);
  END_STEP

  ACTION COMPUTE_FUNCTION_BLOCKS:
    TON0(IN := Step_0_Active, PT := T#6s);
    TON1(IN := Step_1_Active, PT := T#6s);
    TON2(IN := Step_2_Active, PT := T#6s);
  END_ACTION

  TRANSITION FROM Step0 TO Step1
    := TON0.Q;
  END_TRANSITION

  STEP Step1:
    GreenLight(N);
    Step_1_Active(N);
  END_STEP

  TRANSITION FROM Step1 TO Step2
    := TON1.Q;
  END_TRANSITION

  STEP Step2:
    OrangeLight(N);
    Step_2_Active(N);
  END_STEP

  TRANSITION FROM Step2 TO Step0
    := TON2.Q;
  END_TRANSITION

END_FUNCTION_BLOCK
*/

CONFIGURATION Config0

  RESOURCE Res0 ON PLC
    VAR_GLOBAL
      global_status : S_FA_STATUS;
    END_VAR
    TASK task0(INTERVAL := T#20ms,PRIORITY := 0);
    PROGRAM instance0 WITH task0 : program0_POU;
  END_RESOURCE
END_CONFIGURATION
