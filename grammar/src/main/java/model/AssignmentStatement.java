package model;

import java.util.List;
import java.util.ArrayList;

public class AssignmentStatement extends Statement {

    // private String variableQualifier;

    private List<Expression> expressionList = new ArrayList<>();

    /**
     * ctor
     */
    public AssignmentStatement() {
        setStatementType(StatementType.ASSIGNMENT);
    }

    @Override
    public String toString() {
        return "AssignmentStatement [expressionList=" + expressionList + "]";
    }

    // public String getVariableQualifier() {
    //     return variableQualifier;
    // }

    // public void setVariableQualifier(String variableQualifier) {
    //     this.variableQualifier = variableQualifier;
    // }

    public List<Expression> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }
    
}
