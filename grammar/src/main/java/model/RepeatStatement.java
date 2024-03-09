package model;

public class RepeatStatement extends Statement {

    private boolean untilDetected;

    private Expression terminationCondition;

    /**
     * ctor
     */
    public RepeatStatement() {
        setStatementType(StatementType.REPEAT);
    }

    @Override
    public String toString() {
        return "RepeatStatement [untilDetected=" + untilDetected + ", terminationCondition=" + terminationCondition
                + ", statements=" + getStatements() + "]";
    }

    public void addStatement(Statement statement) {
        // if (untilDetected) {
        //     terminationCondition = statement;
        // } else {
        //     getStatements().add(statement);
        // }
        getStatements().add(statement);
    }

    public boolean isUntilDetected() {
        return untilDetected;
    }

    public void setUntilDetected(boolean untilDetected) {
        this.untilDetected = untilDetected;
    }

    public Expression getTerminationCondition() {
        return terminationCondition;
    }

    public void setTerminationCondition(Expression terminationCondition) {
        this.terminationCondition = terminationCondition;
    }

}
