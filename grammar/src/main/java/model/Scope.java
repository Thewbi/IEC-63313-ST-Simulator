package model;

import java.util.List;
import java.util.ArrayList;

public class Scope {

    private List<Variable> variables = new ArrayList<>();

    private List<Statement> statements = new ArrayList<>();

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
