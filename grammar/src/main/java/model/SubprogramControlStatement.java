package model;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.RuntimeMetaData;

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
        throw new RuntimeException();
    }

    public String toString(final int indent) {

        StringBuilder stringBuilder = new StringBuilder();

        addIndent(stringBuilder, indent);
        stringBuilder.append("SubprogramControlStatement [subprogramName=" + subprogramName + "]");

        for (ParameterAssignment parameterAssignment : parameterAssignments) {
            stringBuilder.append("\n");
            addIndent(stringBuilder, indent+1);
            stringBuilder.append(parameterAssignment);
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
    
    public List<ParameterAssignment> getParameterAssignments() {
        return parameterAssignments;
    }
    
}
