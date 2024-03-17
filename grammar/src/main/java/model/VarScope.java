package model;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class VarScope extends DataType {

    private List<Variable> variables = new ArrayList<>();

    private List<Statement> statements = new ArrayList<>();

    public Variable getVariableByName(String name) {

        if (CollectionUtils.isEmpty(variables)) {
            return null;
        }

        for (Variable variable : variables) {
            if (StringUtils.equalsIgnoreCase(variable.getName(), name)) {
                return variable;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(final int indent) {

        StringBuilder stringBuilder = new StringBuilder();

        // addIndent(stringBuilder, indent);
        stringBuilder.append(getClass().getSimpleName() + " name=" + getName() + " ");

        stringBuilder.append("\n");
        addIndent(stringBuilder, indent + 0);
        stringBuilder.append("Variables: ");
        
        if (CollectionUtils.isEmpty(getVariables())) {
            stringBuilder.append("No Variables");
        } else {
            stringBuilder.append("\n");
            for (Variable variable : getVariables()) {

                if (variable.getDataType() instanceof FunctionBlock) {
                    addIndent(stringBuilder, indent + 1);
                    stringBuilder.append(variable.toShortString());
                } else if (variable.getDataType() instanceof Struct) {
                    stringBuilder.append(variable.toString(indent + 1));
                } else {
                    stringBuilder.append(variable.toString(indent + 1));
                }

                stringBuilder.append("\n");
            }
        }

        // stringBuilder.append("\n");
        // addIndent(stringBuilder, indent + 0);
        // stringBuilder.append("Statements: ").append("\n");
        
        if (CollectionUtils.isEmpty(getStatements())) {
            stringBuilder.append("\n");
            addIndent(stringBuilder, indent + 0);
            stringBuilder.append("Statements: ").append("No Statements");
        } else {
            addIndent(stringBuilder, indent + 0);
            stringBuilder.append("Statements: ").append("\n");
            for (Statement statment : getStatements()) {
                // stringBuilder.append("\n");
                stringBuilder.append(statment.toString(indent + 1));
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
