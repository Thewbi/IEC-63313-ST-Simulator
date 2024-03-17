package model;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;

public class Struct extends DataType {

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(final int indent) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n");
        DataType.addIndent(stringBuilder, indent + 0);

        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append(" ");
        stringBuilder.append(getName());

        if (MapUtils.isNotEmpty(getFields())) {

            for (Map.Entry<String, Field> entry : getFields().entrySet()) {

                stringBuilder.append("\n");

                DataType.addIndent(stringBuilder, indent + 1);

                stringBuilder.append(entry.getKey()).append(" = ")
                        .append(entry.getValue().getDataType().getName())
                        .append(" InitialValue: ").append(entry.getValue().getInitialValue());

            }
        }

        return stringBuilder.toString();
    }

}
