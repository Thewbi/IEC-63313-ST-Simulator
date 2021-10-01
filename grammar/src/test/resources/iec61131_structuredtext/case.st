/**/
CASE nState_CreateDB OF
    0:
    	nState_CreateDB := 0;
    1:
    	nState_CreateDB := 1;
    2:
    	nState_CreateDB := 2;
    3:
    	nState_CreateDB := 3;
    100:
    	nState_CreateDB := 100;
    ELSE
    	nState_CreateDB := 101;
END_CASE;


/*
CASE eSeverity OF
    TcEventSeverity.Info:
        stError.sSeverity := 'Info';
    TcEventSeverity.Warning:
        stError.sSeverity := 'Warning';
    TcEventSeverity.Verbose:
        stError.sSeverity := 'Verbose';
    TcEventSeverity.Critical:
        stError.sSeverity := 'Critical';
    TcEventSeverity.Error:
        stError.sSeverity := 'Error';    
END_CASE;
*/