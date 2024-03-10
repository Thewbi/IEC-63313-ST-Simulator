package model;

public class Program extends VarScope {

    private String name;

    @Override
    public String toString() {
        //return "Program [name=" + name + ", variables=" + getVariables() + ", statements=" + getStatements() + "]";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Program [name=" + name + " ");

        stringBuilder.append("\n\n");
        stringBuilder.append("Variables:");
        for (Variable variable : getVariables()) {
            stringBuilder.append("\n").append(variable);
        }

        stringBuilder.append("\n\n");
        stringBuilder.append("Statements:");
        for (Statement statment : getStatements()) {
            //stringBuilder.append("\n");
            stringBuilder.append("\n").append(statment);
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
