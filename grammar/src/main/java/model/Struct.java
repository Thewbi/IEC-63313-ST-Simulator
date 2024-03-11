package model;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;

public class Struct extends DataType {

    @Override
    public String toString() {
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
        
        return stringBuilder.toString();
    }

}
