package model;

import java.util.ArrayList;
import java.util.List;

public class SubprogrammControlStatement extends Statement {

    private String subprogramName;

    private List<ParameterAssignment> parameterAssignments = new ArrayList<>();

    /**
     * ctor
     */
    public SubprogrammControlStatement() {
        setStatementType(StatementType.SUBPROGRAM_CONTROL);
    }

    public void addParameterAssignment(String varName, String expression) {

        ParameterAssignment parameterAssignment = new ParameterAssignment();
        parameterAssignment.setParameterName(varName);
        parameterAssignment.setValue(expression);
        
        parameterAssignments.add(parameterAssignment);
    }

    @Override
    public String toString() {
        //return "SubprogrammControlStatement [subprogramName=" + subprogramName + "]";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SubprogrammControlStatement [subprogramName=" + subprogramName + "]");

        for (ParameterAssignment parameterAssignment : parameterAssignments) {
            stringBuilder.append("\n  ").append(parameterAssignment);
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public String getSubprogramName() {
        return subprogramName;
    }

    public void setSubprogramName(String subprogramName) {
        this.subprogramName = subprogramName;
    }
    
}
