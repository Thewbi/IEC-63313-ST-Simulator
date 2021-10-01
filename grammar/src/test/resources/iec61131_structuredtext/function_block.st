FUNCTION_BLOCK PUBLIC FB_Logger
VAR
  sFilenameExtension : STRING;
END_VAR

// A method starts with a declaration
METHOD PUBLIC Write : STRING
VAR_INPUT
  sMsg : STRING;
END_VAR

// A method declaration is followed by statements that form
// the method's body.
// The next method declaration implicitly terminates the
// preceding method's body!
IF G = 0 THEN
	SEL := 1;
ELSE
	SEL := 2;
END_IF;

/*
sMsg := CONCAT('logger: ', sMsg);
IF (sFilenameExtension = 'txt') THEN
  Write := WriteTxt(sMsg);
ELSIF (sFilenameExtension = 'csv') THEN
  Write := WriteCsv(sMsg);
ELSIF (sFilenameExtension = 'xml') THEN
  Write := WriteXml(sMsg);
END_IF
*/

METHOD CreateErrorLogDB : BOOL
VAR_INPUT
    sDBName : T_MaxString;
END_VAR

CASE nState_CreateDB OF
    0:
    	stDBConfig.sServer := CONCAT(CONCAT(sDBPath, sDBName), '.sdf');
    	stDBConfig.bAuthentification := FALSE;
    	
    	nState_CreateDB := 1;
    1:
        IF fbPLCDBCreate.Database(pDatabaseConfig:= ADR(stDBConfig), 
            cbDatabaseConfig:= SIZEOF(stDBConfig), 
            bCreateXMLConfig:= FALSE, 
            pDBID:= 0) THEN            
            ipResultEvt := fbPLCDBCreate.ipTcResult;
            IF fbPLCDBCreate.bError THEN
                nState_CreateDB := 100;
            ELSE
                nState_CreateDB := 2;
            END_IF;           
        END_IF;
     2:
        IF fbConfigTcDBSrv.Create(pTcDBSrvConfig:= ADR(stDBConfig), 
            cbTcDBSrvConfig:= SIZEOF(stDBConfig), bTemporary:= TRUE,
             pConfigID:= ADR(nDBID) ) THEN
            ipResultEvt := fbConfigTcDBSrv.ipTcResult;
            IF fbConfigTcDBSrv.bError THEN
                nState_CreateDB := 100;
            ELSE
                nState_CreateDB := 3;
            END_IF;
        END_IF;
     3:
     	arrTableColumns[0].sName := 'ID';
        arrTableColumns[0].eType := E_ColumnType.BigInt;
        arrTableColumns[0].nLength := 8;
        arrTableColumns[0].sProperty := 'IDENTITY(1,1)';
        
        arrTableColumns[1].sName := 'Timestamp';
        arrTableColumns[1].eType := E_ColumnType.DateTime;
        arrTableColumns[1].nLength := 4;
        
        arrTableColumns[2].sName := 'Severity';
        arrTableColumns[2].eType := E_ColumnType.NVarChar;
        arrTableColumns[2].nLength := 10;
        
        arrTableColumns[3].sName := 'ErrorCode';
        arrTableColumns[3].eType := E_ColumnType.Integer;
        arrTableColumns[3].nLength := 4;
        
        arrTableColumns[4].sName := 'Message';
        arrTableColumns[4].eType := E_ColumnType.NVarChar;
        arrTableColumns[4].nLength := 255;
                
        IF fbPLCDBCreate.Table(hDBID:= nDBID, sTableName:= sTableName, 
            pTableCfg:= ADR(arrTableColumns),
            cbTableCfg:= SIZEOF(arrTableColumns)) THEN
            ipResultEvt := fbPLCDBCreate.ipTcResultEvent;
            nState_CreateDB := 100;            
        END_IF;
    100:
        IF _SetResultInfo(1033) THEN
            IF NOT bError THEN
                _bHasCreated := TRUE;
            END_IF;

            nState_CreateDB := 0;
        END_IF;
END_CASE;

END_FUNCTION_BLOCK
