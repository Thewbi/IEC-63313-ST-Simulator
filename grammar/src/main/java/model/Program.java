package model;

import java.util.List;
import java.util.ArrayList;

public class Program {

    private String name;

    private List<Variable> variables = new ArrayList<>();

    private List<Statement> statements = new ArrayList<>();

    @Override
    public String toString() {
        return "Program [name=" + name + ", variables=" + variables + ", statements=" + statements + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
