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

        addIndent(stringBuilder, indent);
        stringBuilder.append(getClass().getSimpleName() + " [name=" + getName() + " ");

        stringBuilder.append("\n");
        addIndent(stringBuilder, indent + 1);
        stringBuilder.append("Variables: ");
        if (CollectionUtils.isEmpty(getVariables())) {
            stringBuilder.append("No Variables");
        } else {
            stringBuilder.append("\n");
            for (Variable variable : getVariables()) {

                if (variable.getDataType() instanceof FunctionBlock) {

                    // stringBuilder.append("\n");
                    addIndent(stringBuilder, indent + 2);
                    stringBuilder.append(variable.toShortString());

                } else {

                    // stringBuilder.append("\n");
                    // addIndent(stringBuilder, indent+2);
                    stringBuilder.append(variable.toString(indent + 2));
                }

                stringBuilder.append("\n");
            }
        }

        stringBuilder.append("\n");
        addIndent(stringBuilder, indent + 1);
        stringBuilder.append("Statements: ");
        if (CollectionUtils.isEmpty(getStatements())) {
            stringBuilder.append("No Statements");
        } else {

            for (Statement statment : getStatements()) {
                stringBuilder.append("\n");
                stringBuilder.append(statment.toString(indent + 2));
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
