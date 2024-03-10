package model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

public class DataType {

    private String name;

    //private Map<String, DataType> fields = new HashMap<>();
    private Map<String, Field> fields = new HashMap<>();

    private String initialValue;

    @Override
    public String toString() {
        // return "TypeScope [typeMap=" + typeMap + "]";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);

        if (MapUtils.isNotEmpty(fields)) {
            for (Map.Entry<String, Field> entry : fields.entrySet()) {
                stringBuilder.append("  ").append(entry.getKey()).append(" = ").append(entry.getValue());
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public void addField(String fieldName, DataType dataType, String initialValue) {
        if (fields.containsKey(fieldName)) {
            throw new RuntimeException("Field \"" + fieldName + "\" already contained!");
        }

        Field field = new Field();
        field.setDataType(dataType);
        field.setInitialValue(initialValue);

        fields.put(fieldName, field);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

}
