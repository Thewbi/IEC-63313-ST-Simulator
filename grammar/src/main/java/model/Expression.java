package model;

import java.util.List;

import org.antlr.v4.runtime.RuntimeMetaData;

import java.util.ArrayList;

public class Expression {

    private ExpressionType expressionType = ExpressionType.UNKNOWN;

    private Integer integerValue;

    private Boolean booleanValue;

    private String variableNameValue;

    private List<Expression> expressionList = new ArrayList<>();

    @Override
    public String toString() {

        throw new RuntimeException("");

        // int indent = 1;

        // StringBuilder stringBuilder = new StringBuilder();
        // toStringSelf(stringBuilder, indent);

        // for (Expression childExpression : expressionList) {
        //     stringBuilder.append(childExpression.toString(indent + 1));
        // }

        // return stringBuilder.toString();
    }

    public String toString(int indent) {

        StringBuilder stringBuilder = new StringBuilder();

        DataType.addIndent(stringBuilder, indent);

        toStringSelf(stringBuilder, indent);

        for (Expression childExpression : expressionList) {
            stringBuilder.append(childExpression.toString(indent + 1));
        }

        return stringBuilder.toString();
    }

    private void toStringSelf(StringBuilder stringBuilder, int indent) {

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

        stringBuilder.append(" ]\n");
    }

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

}
