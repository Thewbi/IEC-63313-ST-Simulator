package model;

import org.apache.commons.collections4.CollectionUtils;

import instance.VariableInstance;

public class Action extends VarScope implements StatementContainer {

    private String name;

    private boolean executed;

    private VariableInstance parentVariableInstance;

    public void execute(TypeScope globalTypeScope) {

        if (executed) {
            return;
        }

        executed = true;

        if (CollectionUtils.isEmpty(getStatements())) {
            return;
        }

        System.out.println("Executing action: \"" + name + "\"");

        // execute the statements in the context of the output variableInstance (== FunctionBlock)
        executeStatements(globalTypeScope, parentVariableInstance, getStatements());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Action other = (Action) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public VariableInstance getParentVariableInstance() {
        return parentVariableInstance;
    }

    public void setParentVariableInstance(VariableInstance parentVariableInstance) {
        this.parentVariableInstance = parentVariableInstance;
    }
}
