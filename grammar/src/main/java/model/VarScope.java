package model;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;

public class VarScope extends DataType {

    private List<Variable> variables = new ArrayList<>();

    private List<Statement> statements = new ArrayList<>();

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getClass().getSimpleName() + " [name=" + getName() + " ");

        stringBuilder.append("\n\n");
        stringBuilder.append("Variables:");
        if (CollectionUtils.isEmpty(getVariables())) {
            stringBuilder.append("No Variables");
        } else {
            for (Variable variable : getVariables()) {

                if (variable.getDataType() instanceof FunctionBlock) {
                    stringBuilder.append("\n").append(variable.toShortString());
                } else {
                    stringBuilder.append("\n").append(variable);
                }
            }
        }

        stringBuilder.append("\n\n");
        stringBuilder.append("Statements:");
        if (CollectionUtils.isEmpty(getStatements())) {
            stringBuilder.append("No Statements");
        } else {
            for (Statement statment : getStatements()) {
                stringBuilder.append("\n").append(statment);
            }
        }

        return stringBuilder.toString();
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }
    
}
