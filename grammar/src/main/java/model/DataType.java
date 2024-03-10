package model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

public class DataType {

    private String name;

    private Map<String, DataType> fields = new HashMap<>();

    @Override
    public String toString() {
        // return "TypeScope [typeMap=" + typeMap + "]";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);

        if (MapUtils.isNotEmpty(fields)) {
            for (Map.Entry<String, DataType> entry : fields.entrySet()) {
                stringBuilder.append("  ").append(entry.getKey()).append(" = ").append(entry.getValue());
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addField(String fieldName, DataType dataType) {
        if (fields.containsKey(fieldName)) {
            throw new RuntimeException("Field \"" + fieldName + "\" already contained!");
        }
        fields.put(fieldName, dataType);
    }

}
