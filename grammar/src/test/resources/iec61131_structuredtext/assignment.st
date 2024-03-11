PROGRAM program0_POU
    VAR
        FA_Start_T_HMI : BOOL;
    END_VAR

    //test2 := 4 + 14;
    //test2 := 4 - 14;
    //test2 := 4 * 14;
    //test2 := 4 / 14;

    //global_status.BART_Hand := FA_BART_Hand_T_HMI;

    //_TMP_AND15_OUT := AND(_TMP_NOT17_OUT, Hand_P2);

    //_TMP_FA_SERV1_OUT := FA_SERV(global_status, FA_BART_Hand_T_HMI, FA_BART_Auto_T_HMI, FA_Stoerung_T_HMI, FA_Stoer_Quit_T_HMI, FA_NotHalt_T_HMI);

    //FA_SERV(global_status, FA_BART_Hand_T_HMI, FA_BART_Auto_T_HMI, FA_Stoerung_T_HMI, FA_Stoer_Quit_T_HMI, FA_NotHalt_T_HMI);

    AKT_5_2_MV0(status := global_status, Hand_P1 := Zyl1_T_P1_HMI, Hand_P2 := Zyl1_T_P2_HMI, SEN_P1 := SEN_P1_T_HMI, SEN_P2 := SEN_P2_T_HMI);

END_PROGRAM