package instance;

public class VariableDescriptor {

    public boolean external;

    public boolean retain;

    public VariableInstance variableInstance;

    @Override
    public String toString() {
        return "VariableDescriptor [external=" + external + ", retain=" + retain + ", variableInstance="
                + variableInstance.getName() + " = " + variableInstance.getValue() + "]";
    }

}
