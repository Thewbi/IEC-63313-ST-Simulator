package model;

public class Statement extends VarScope {

    private StatementType statementType;

    public StatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(StatementType statementType) {
        this.statementType = statementType;
    }
    
}
