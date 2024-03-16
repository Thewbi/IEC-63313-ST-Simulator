package model;

import java.util.List;
import java.util.ArrayList;

public class Transition {

    private String name;

    private String sourceStepName;

    private List<String> targetStepNames = new ArrayList<>();

    public List<Expression> expressionList = new ArrayList<>();

    public String toString(int indent) {

        StringBuilder stringBuilder = new StringBuilder();
        DataType.addIndent(stringBuilder, indent);
        stringBuilder.append("Transition").append("\n");
        
        DataType.addIndent(stringBuilder, indent+1);
        stringBuilder.append("name = " + name).append("\n");

        DataType.addIndent(stringBuilder, indent+1);
        stringBuilder.append("sourceStepName = " + sourceStepName).append("\n");

        DataType.addIndent(stringBuilder, indent+1);
        stringBuilder.append("targetStepName = ");
        for (String targetStep : targetStepNames) {
            stringBuilder.append(targetStep).append(", ");
        }
        stringBuilder.append("\n");

        DataType.addIndent(stringBuilder, indent+1);
        stringBuilder.append("expression = ");
        stringBuilder.append("\n");
        for (Expression expression : expressionList) {
            stringBuilder.append(expression.toString(indent+2));
        }
        // stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceStepName() {
        return sourceStepName;
    }

    public void setSourceStepName(String sourceStepName) {
        this.sourceStepName = sourceStepName;
    }

    public List<String> getTargetStepNames() {
        return targetStepNames;
    }

    public void setTargetStepNames(List<String> targetStepNames) {
        this.targetStepNames = targetStepNames;
    }

    public List<Expression> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }

}
