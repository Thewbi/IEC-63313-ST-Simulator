package model;

import java.util.List;

import java.util.ArrayList;

public class Expression {

    private ExpressionType expressionType = ExpressionType.UNKNOWN;

    private Integer integerValue;

    private Boolean booleanValue;

    private String variableNameValue;

    @Override
    public String toString() {

        int indent = 1;

        StringBuilder stringBuilder = new StringBuilder();
        toStringSelf(stringBuilder, indent);

        for (Expression childExpression : expressionList) {
            stringBuilder.append(childExpression.toString(indent + 1));
        }

        return stringBuilder.toString();
    }

    public String toString(int indent) {

        StringBuilder stringBuilder = new StringBuilder();
        toStringSelf(stringBuilder, indent);

        for (Expression childExpression : expressionList) {
            stringBuilder.append(childExpression.toString(indent + 1));
        }

        return stringBuilder.toString();
    }

    private void toStringSelf(StringBuilder stringBuilder, int indent) {

        for (int i = 0; i < indent; i++)
        {
            stringBuilder.append("  ");
        }
        stringBuilder.append("Expression [ ");
        stringBuilder.append(" expressionType=" + expressionType + " ");

        if (integerValue != null) {
            stringBuilder.append(" integerValue=" + integerValue + " ");
        }

        if (booleanValue != null) {
            stringBuilder.append(" booleanValue=" + booleanValue + " ");
        }

        if (variableNameValue != null) {
            stringBuilder.append(" variableNameValue=" + variableNameValue + " ");
        }

        // if (subprogramName != null) {
        //     stringBuilder.append(" subprogramName=" + subprogramName + " ");
        // }

        stringBuilder.append(" ]\n");
    }

    private List<Expression> expressionList = new ArrayList<>();

    public List<Expression> getExpressionList() {
        return expressionList;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public String getVariableNameValue() {
        return variableNameValue;
    }

    public void setVariableNameValue(String variableNameValue) {
        this.variableNameValue = variableNameValue;
    }

    // public String getSubprogramName() {
    //     return subprogramName;
    // }

    // public void setSubprogramName(String subprogramName) {
    //     this.subprogramName = subprogramName;
    // }

}
