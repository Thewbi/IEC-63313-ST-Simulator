package model;

import java.io.ObjectInputStream.GetField;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

public class Struct extends DataType {

    // @Override
    // public String toString() {
    // StringBuilder stringBuilder = new StringBuilder();

    // stringBuilder.append(getClass().getSimpleName() + " [name=" + getName() + "
    // ");
    // stringBuilder.append("\n");

    // stringBuilder.append("Fields:\n");
    // if (CollectionUtils.isEmpty(GetField())) {
    // stringBuilder.append("No Variables");
    // } else {
    // for (Variable variable : getVariables()) {

    // if (variable.getDataType() instanceof FunctionBlock) {
    // stringBuilder.append("\n").append(variable.toShortString());
    // } else {
    // stringBuilder.append("\n").append(variable);
    // }
    // }
    // }
    // }

    @Override
    public String toString() {
        // return "TypeScope [typeMap=" + typeMap + "]";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append(" ");
        stringBuilder.append(getName());
        stringBuilder.append("\n");

        if (MapUtils.isNotEmpty(getFields())) {
            for (Map.Entry<String, Field> entry : getFields().entrySet()) {
                stringBuilder.append("  ").append(entry.getKey()).append(" = ")
                        .append(entry.getValue().getDataType().getName())
                        .append(" InitialValue: ").append(entry.getValue().getInitialValue());
                stringBuilder.append("\n");
            }
        }

        // System.out.println(stringBuilder.toString());

        return stringBuilder.toString();
    }

}
