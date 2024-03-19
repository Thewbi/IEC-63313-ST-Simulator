TYPE
  S_FA_STATUS : STRUCT
    BART_Hand : BOOL := TRUE;
    BART_Auto : BOOL;
    Stoerung : BOOL;
    Stoer_Quit : BOOL;
    NotHalt : BOOL;
    Takt : BOOL;
    Transition_EN : BOOL;
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
    Auto_P1 : BOOL;
    Auto_P2 : BOOL;
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
    F_PAAR : BOOL;
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
    _TMP_AND40_OUT : BOOL;
    _TMP_OR39_OUT : BOOL;
    _TMP_AND1_OUT : BOOL;
    _TMP_AND46_OUT : BOOL;
    _TMP_OR44_OUT : BOOL;
    _TMP_OR10_OUT : BOOL;
    _TMP_NOT17_OUT : BOOL;
    _TMP_NOT47_OUT : BOOL;
    _TMP_AND14_OUT : BOOL;
    _TMP_NOT25_OUT : BOOL;
    _TMP_AND23_OUT : BOOL;
    _TMP_OR28_OUT : BOOL;
    _TMP_OR37_OUT : BOOL;
    _TMP_AND33_OUT : BOOL;
  END_VAR

  _TMP_AND6_OUT := AND(status.BART_Hand, Hand_P2);
  _TMP_AND40_OUT := AND(status.BART_Auto, Auto_P2);
  _TMP_OR39_OUT := OR(_TMP_AND6_OUT, _TMP_AND40_OUT);
  _TMP_AND1_OUT := AND(status.BART_Hand, Hand_P1);
  _TMP_AND46_OUT := AND(status.BART_Auto, Auto_P1);
  _TMP_OR44_OUT := OR(_TMP_AND1_OUT, _TMP_AND46_OUT);
  _TMP_OR10_OUT := OR(_TMP_OR44_OUT, status.NotHalt);
  s_MV_P2_FF(S1 := _TMP_OR39_OUT, R := _TMP_OR10_OUT);
  s_MV_P2 := s_MV_P2_FF.Q1;
  _TMP_NOT17_OUT := NOT(SEN_P1);
  _TMP_NOT47_OUT := NOT(s_MV_P2);
  _TMP_AND14_OUT := AND(_TMP_NOT17_OUT, _TMP_NOT47_OUT);
  TON0(IN := _TMP_AND14_OUT, PT := const_LaufzeitFehler_T);
  RS0(S := TON0.Q, R1 := status.Stoer_Quit);
  F_LZ_P1 := RS0.Q1;
  _TMP_NOT25_OUT := NOT(SEN_P2);
  _TMP_AND23_OUT := AND(_TMP_NOT25_OUT, s_MV_P2);
  TON1(IN := _TMP_AND23_OUT, PT := const_LaufzeitFehler_T);
  RS1(S := TON1.Q, R1 := status.Stoer_Quit);
  F_LZ_P2 := RS1.Q1;
  _TMP_OR28_OUT := OR(F_LZ_P1, F_LZ_P2);
  _TMP_OR37_OUT := OR(_TMP_OR28_OUT, F_PAAR);
  F_LZ := _TMP_OR37_OUT;
  _TMP_AND33_OUT := AND(SEN_P1, SEN_P2);
  F_PAAR := _TMP_AND33_OUT;
END_FUNCTION_BLOCK

FUNCTION_BLOCK FB_Automatik
  VAR_OUTPUT
    Zylinder_1_Auto_P1 : BOOL;
    Zylinder_1_Auto_P2 : BOOL;
  END_VAR
  VAR_INPUT
    S_FA_Status : S_FA_STATUS;
    Zylinder_1_SEN_P1 : BOOL;
    Zylinder_1_SEN_P2 : BOOL;
  END_VAR
  VAR
    _TMP_NOT9_OUT : BOOL;
    _TMP_NOT8_OUT : BOOL;
    _TMP_AND10_OUT : BOOL;
    _TMP_AND11_OUT : BOOL;
    _TMP_NOT16_OUT : BOOL;
  END_VAR

  INITIAL_STEP Step0:
    Zylinder_1_Auto_P1(R);
    Zylinder_1_Auto_P2(R);
    COMPUTE_FUNCTION_BLOCKS(S);
  END_STEP

  ACTION COMPUTE_FUNCTION_BLOCKS:
    _TMP_NOT9_OUT := NOT(S_FA_Status.Stoerung);
    _TMP_NOT8_OUT := NOT(S_FA_Status.NotHalt);
    _TMP_AND10_OUT := AND(_TMP_NOT9_OUT, _TMP_NOT8_OUT);
    _TMP_AND11_OUT := AND(S_FA_Status.BART_Auto, _TMP_AND10_OUT);
    _TMP_NOT16_OUT := NOT(Zylinder_1_SEN_P1);
  END_ACTION

  TRANSITION FROM Step0 TO Step1
    := _TMP_AND11_OUT;
  END_TRANSITION

  STEP Step1:
  END_STEP

  TRANSITION FROM Step1 TO Step2
    := _TMP_NOT16_OUT;
  END_TRANSITION

  STEP Step2:
    Zylinder_1_Auto_P1(N);
  END_STEP

  TRANSITION FROM Step2 TO Step3
    := Zylinder_1_SEN_P1;
  END_TRANSITION

  STEP Step3:
    Zylinder_1_Auto_P2(N);
  END_STEP

  TRANSITION FROM Step3 TO Step4
    := Zylinder_1_SEN_P2;
  END_TRANSITION

  STEP Step4:
  END_STEP

  TRANSITION FROM Step1 TO Step3
    := Zylinder_1_SEN_P1;
  END_TRANSITION

END_FUNCTION_BLOCK

FUNCTION FA_SERV : BOOL
  VAR_IN_OUT
    status : S_FA_STATUS;
  END_VAR
  VAR_INPUT
    BART_Hand : BOOL;
    BART_Auto : BOOL;
    Stoer_Quit : BOOL;
  END_VAR

  status.BART_Hand := BART_Hand;
  status.Stoer_Quit := Stoer_Quit;
  status.BART_Auto := BART_Auto;
END_FUNCTION

PROGRAM program0
  VAR_EXTERNAL
    global_status : S_FA_STATUS;
  END_VAR
  VAR
    FA_BART_Hand_T_HMI : BOOL := TRUE;
    FA_BART_Auto_S_HMI : BOOL;
    FA_Stoer_Quit_T_HMI : BOOL;
    Zyl1_T_P1_HMI : BOOL := TRUE;
    Zyl1_AUTO_P1_HMI : BOOL := TRUE;
    SEN_P1_T_HMI : BOOL := TRUE;
    Zyl1_T_P2_HMI : BOOL;
    Zyl1_AUTO_P2_HMI : BOOL := TRUE;
    SEN_P2_T_HMI : BOOL;
    AKT_5_2_MV0 : AKT_5_2_MV;
    s_MV_P2 : BOOL := FALSE;
    Diag : Diag;
    FB_Automatik0 : FB_Automatik;
    _TMP_FA_SERV1_OUT : BOOL;
  END_VAR

  AKT_5_2_MV0(status := global_status, Hand_P1 := Zyl1_T_P1_HMI, Hand_P2 := Zyl1_T_P2_HMI, Auto_P1 := Zyl1_AUTO_P1_HMI, Auto_P2 := Zyl1_AUTO_P2_HMI, SEN_P1 := SEN_P1_T_HMI, SEN_P2 := SEN_P2_T_HMI);
  s_MV_P2 := AKT_5_2_MV0.s_MV_P2;
  Diag.Stoerung := AKT_5_2_MV0.F_LZ;
  FB_Automatik0(S_FA_Status := global_status, Zylinder_1_SEN_P1 := SEN_P1_T_HMI, Zylinder_1_SEN_P2 := SEN_P2_T_HMI);
  Zyl1_AUTO_P1_HMI := FB_Automatik0.Zylinder_1_Auto_P1;
  Zyl1_AUTO_P2_HMI := FB_Automatik0.Zylinder_1_Auto_P2;
  _TMP_FA_SERV1_OUT := FA_SERV(status := global_status, BART_Hand := FA_BART_Hand_T_HMI, BART_Auto := FA_BART_Auto_S_HMI, Stoer_Quit := FA_Stoer_Quit_T_HMI);
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
