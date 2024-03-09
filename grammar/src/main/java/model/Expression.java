package model;

import java.util.List;
import java.util.ArrayList;

public class Expression {

    private ExpressionType expressionType = ExpressionType.UNKNOWN;

    private Integer integerValue;

    private Boolean booleanValue;

    @Override
    public String toString() {
        return "Expression [expressionType=" + expressionType + ", integerValue=" + integerValue + ", booleanValue="
                + booleanValue + ", expressionList=" + expressionList + "]";
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
    
}
