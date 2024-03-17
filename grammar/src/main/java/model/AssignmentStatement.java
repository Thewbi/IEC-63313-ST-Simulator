package model;

import java.util.List;
import java.util.ArrayList;

public class AssignmentStatement extends Statement {

    private String variable;

    private List<Expression> expressionList = new ArrayList<>();

    /**
     * ctor
     */
    public AssignmentStatement() {
        setStatementType(StatementType.ASSIGNMENT);
    }

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    @Override
    public String toString(final int indent) {
        //return "AssignmentStatement [expressionList=" + expressionList + "]";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AssignmentStatement\n");
        // System.out.println("AssignmentStatement\n");

        stringBuilder.append("  variable: " + variable + "\n");

        stringBuilder.append("  expression: \n");
        for (Expression childExpression : expressionList) {
            // int indent = 1;
            // printExpression(childExpression, indent, stringBuilder);
            stringBuilder.append(childExpression);
            // System.out.println(childExpression);
        }

        return stringBuilder.toString();
    }

    private void printExpression(Expression expression, int indent, StringBuilder stringBuilder) {
        for (int i = 0; i < indent; i++)
        {
            stringBuilder.append("  ");
            // System.out.print("  ");
        }
        stringBuilder.append(expression).append("\n\n");
        // System.out.print(expression);
        // System.out.print("\n\n");
        for (Expression childExpression : expression.getExpressionList()) {
            printExpression(childExpression, indent + 1, stringBuilder);
        }
    }    

    public List<Expression> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }
    
    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}
