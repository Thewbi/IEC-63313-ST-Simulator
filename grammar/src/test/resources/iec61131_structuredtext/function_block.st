FUNCTION_BLOCK PUBLIC FB_Logger
VAR
  sFilenameExtension : STRING;
END_VAR

METHOD PUBLIC Write : STRING
VAR_INPUT
  sMsg : STRING;
END_VAR

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

END_FUNCTION_BLOCK