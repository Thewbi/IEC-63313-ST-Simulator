package instance;

public class SRVariableInstance extends VariableInstance {

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getName());
        stringBuilder.append("\n");

        for (VariableDescriptor variableDescriptor : getElements().values()) {

            // indent
            stringBuilder.append("  ");

            stringBuilder.append(variableDescriptor.variableInstance.getName() + " "
                    + variableDescriptor.variableInstance.getValue());

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

}
