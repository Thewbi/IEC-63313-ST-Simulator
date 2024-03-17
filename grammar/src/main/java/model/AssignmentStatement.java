package model;

import java.util.ArrayList;
import java.util.List;

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

        StringBuilder stringBuilder = new StringBuilder();

        addIndent(stringBuilder, indent);
        stringBuilder.append("AssignmentStatement\n");

        addIndent(stringBuilder, indent + 1);
        stringBuilder.append("variable: " + variable + "\n");

        addIndent(stringBuilder, indent + 1);
        stringBuilder.append("expression: \n");
        for (Expression childExpression : expressionList) {
            addIndent(stringBuilder, indent + 1);
            stringBuilder.append(childExpression);
        }

        return stringBuilder.toString();
    }

    private void printExpression(Expression expression, int indent, StringBuilder stringBuilder) {
        for (int i = 0; i < indent; i++) {
            stringBuilder.append("  ");
            // System.out.print(" ");
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
