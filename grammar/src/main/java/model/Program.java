package model;

import java.util.List;
import java.util.ArrayList;

public class Program extends Scope {

    private String name;

    @Override
    public String toString() {
        //return "Program [name=" + name + ", variables=" + getVariables() + ", statements=" + getStatements() + "]";

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("Program [name=" + name + " ");

        stringBuffer.append("\n\n");
        stringBuffer.append("Variables:");
        for (Variable variable : getVariables()) {
            stringBuffer.append("\n\n").append(variable);
        }

        stringBuffer.append("\n\n");
        stringBuffer.append("Statements:");
        for (Statement statment : getStatements()) {
            stringBuffer.append("\n\n\n\n");
            stringBuffer.append(statment);
        }

        return stringBuffer.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
