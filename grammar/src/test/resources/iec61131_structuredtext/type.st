TYPE ST_CAN_RXTX :
STRUCT
	uiTXCounter				:	UINT;
	uiRxCounter				:	UINT;
	uiNoOfRxTxMessages		:	UINT;
//	ar_stRxTxMessage		:	ARRAY[0..9] OF ST_CAN_RXTX_MESSAGE;
END_STRUCT;
END_TYPE

TYPE ST_CAN_RXTX_MESSAGE :
STRUCT
	uiLength		:	UINT;
	udCobId			:	UDINT;
//	ar_usiData		:	ARRAY[0..7] OF USINT;
END_STRUCT;
END_TYPE

TYPE ST_GezeFTV :
STRUCT
	bEntriegelt					:	BOOL;
	bVerriegelt					:	BOOL;
	bRunAlarm					:	BOOL;
	bTuerZu						:	BOOL;	// 1/4
	bRueckmeldungVerriegelung	:	BOOL;	// 1/7
	bTuerAlarmZentrale			:	BOOL;	// 2/2
	bKurzzeitfreigabe			:	BOOL;
END_STRUCT;
END_TYPE

