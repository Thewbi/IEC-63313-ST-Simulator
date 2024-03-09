package model;

public class AssignmentStatement extends Statement {

    private String variableQualifier;

    private Expression expression;

    /**
     * ctor
     */
    public AssignmentStatement() {
        setStatementType(StatementType.ASSIGNMENT);
    }

    public String getVariableQualifier() {
        return variableQualifier;
    }

    public void setVariableQualifier(String variableQualifier) {
        this.variableQualifier = variableQualifier;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "AssignmentStatement [variableQualifier=" + variableQualifier + ", expression=" + expression + "]";
    }
    
}
