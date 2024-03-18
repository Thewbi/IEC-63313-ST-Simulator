package instance;

import model.DataType;

public class VariableDescriptor {

    public boolean external;

    public boolean retain;

    public VariableInstance variableInstance;

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(int indent) {

        StringBuilder stringBuilder = new StringBuilder();

        DataType.addIndent(stringBuilder, indent);

        stringBuilder.append("VariableDescriptor [external=" + external + ", retain=" + retain);

        // return "VariableDescriptor [external=" + external + ", retain=" + retain + ", variableInstance="
        //         + variableInstance.toString(indent + 1) + "]";

        stringBuilder.append("\n");
        stringBuilder.append(variableInstance.toString(indent+1));

        return stringBuilder.toString();
    }

}
