TYPE
  S_FA_STATUS : STRUCT
    BART_Hand : BOOL := TRUE;
    BART_Auto : BOOL;
    Stoerung : BOOL;
    Stoer_Quit : BOOL;
    NotHalt : BOOL;
    Takt : BOOL;
  END_STRUCT;
END_TYPE

FUNCTION FA_SERV : BOOL
  VAR_IN_OUT
    status : S_FA_STATUS;
  END_VAR
  VAR_INPUT
    BART_Hand : BOOL;
  END_VAR

  status.BART_Hand := BART_Hand;
END_FUNCTION

PROGRAM program0
  VAR_EXTERNAL
    global_status : S_FA_STATUS;
  END_VAR
  VAR
    FA_BART_Hand_T_HMI : BOOL := TRUE;
    _TMP_FA_SERV1_OUT : BOOL;
  END_VAR

  _TMP_FA_SERV1_OUT := FA_SERV(status := global_status, BART_Hand := FA_BART_Hand_T_HMI);
  //_TMP_FA_SERV1_OUT := FA_SERV(global_status, FA_BART_Hand_T_HMI);
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
