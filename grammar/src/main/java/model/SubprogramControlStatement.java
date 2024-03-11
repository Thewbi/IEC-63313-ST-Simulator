package model;

import java.util.ArrayList;
import java.util.List;

public class SubprogramControlStatement extends Statement {

    private String subprogramName;

    private List<ParameterAssignment> parameterAssignments = new ArrayList<>();

    /**
     * ctor
     */
    public SubprogramControlStatement() {
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
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SubprogramControlStatement [subprogramName=" + subprogramName + "]");

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
