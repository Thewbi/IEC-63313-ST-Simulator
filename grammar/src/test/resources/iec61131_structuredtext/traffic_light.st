/**
 * The sequence chart / state machine
 */
FUNCTION_BLOCK SequentialFunctionChart
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
    TON1(IN := Step_1_Active, PT := T#7s);
    TON2(IN := Step_2_Active, PT := T#8s);
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

/**
 * The program
 */
PROGRAM program0_POU
  VAR
    SequentialFunctionChart0 : SequentialFunctionChart;
  END_VAR
  SequentialFunctionChart0();
END_PROGRAM

/**
 * Config
 */
CONFIGURATION Config0
  RESOURCE Res0 ON PLC
    //VAR_GLOBAL
    //  global_status : S_FA_STATUS;
    //END_VAR
    TASK task0(INTERVAL := T#20ms, PRIORITY := 0);
    PROGRAM instance0 WITH task0 : program0_POU;
  END_RESOURCE
END_CONFIGURATION