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

FUNCTION FA_SERV : BOOL
  VAR_INPUT
    status : S_FA_STATUS;
    BART_Hand : BOOL;
    BART_Auto : BOOL;
    Stoerung : BOOL;
    Stoer_Quit : BOOL;
    NotHalt : BOOL;
  END_VAR

/*
  status.BART_Hand := BART_Hand;
  FA_SERV := status.BART_Hand;
  status.BART_Auto := BART_Auto;
  status.Stoerung := Stoerung;
  status.Stoer_Quit := Stoer_Quit;
  */
  status.NotHalt := NotHalt;

END_FUNCTION

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
    Zyl1_T_P1_HMI : BOOL := FALSE;
    Zyl1_T_P2_HMI : BOOL := FALSE;
    s_MV_P2_FF : BOOL;
    SEN_P1_T_HMI : BOOL := TRUE;
    SEN_P2_T_HMI : BOOL := FALSE;
    Diag : Diag;
    _TMP_FA_SERV1_OUT : BOOL;
  END_VAR

  _TMP_FA_SERV1_OUT := FA_SERV(global_status, FA_BART_Hand_T_HMI, FA_BART_Auto_T_HMI, FA_Stoerung_T_HMI, FA_Stoer_Quit_T_HMI, FA_NotHalt_T_HMI);
  DebugBoolean := _TMP_FA_SERV1_OUT;
  
END_PROGRAM

CONFIGURATION Config0
  RESOURCE Res0 ON PLC
    VAR_GLOBAL
      global_status : S_FA_STATUS;
    END_VAR
    TASK task0(INTERVAL := T#20ms, PRIORITY := 0);
    PROGRAM instance0 WITH task0 : program0_POU;
  END_RESOURCE
END_CONFIGURATION
