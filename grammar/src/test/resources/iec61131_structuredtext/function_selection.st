FUNCTION SEL : ANY (* binary selection *)
  VAR_INPUT
    G : BOOL;
    IN0 : ANY;
    IN1 : ANY;
  END_VAR
  IF G = 0 THEN
    SEL := IN0; (* selection of upper input *)
  ELSE
    SEL := IN1; (*selection of lower input *)
  END_IF;
END_FUNCTION