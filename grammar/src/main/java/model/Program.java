package model;

import java.util.List;
import java.util.ArrayList;

public class Program extends Scope {

    private String name;

    @Override
    public String toString() {
        //return "Program [name=" + name + ", variables=" + getVariables() + ", statements=" + getStatements() + "]";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Program [name=" + name + " ");

        stringBuilder.append("\n\n");
        stringBuilder.append("Variables:");
        for (Variable variable : getVariables()) {
            stringBuilder.append("\n\n").append(variable);
        }

        stringBuilder.append("\n\n");
        stringBuilder.append("Statements:");
        for (Statement statment : getStatements()) {
            stringBuilder.append("\n\n\n\n");
            stringBuilder.append(statment);
        }

        return stringBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
