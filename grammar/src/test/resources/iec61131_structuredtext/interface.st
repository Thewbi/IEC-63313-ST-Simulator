INTERFACE I_Subject

METHOD Attach : BOOL
VAR_INPUT
	ipObserver		: I_Observer;
END_VAR

METHOD Detach : BOOL
VAR_INPUT	
	ipObserver		: I_Observer;
END_VAR
