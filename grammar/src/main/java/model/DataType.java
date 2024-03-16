package model;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.RuntimeMetaData;
import org.apache.commons.collections4.MapUtils;

public abstract class DataType {

    private String name;

    private Map<String, Field> fields = new HashMap<>();

    private String initialValue;

    public static void addIndent(StringBuilder stringBuilder, int indent) {
        for (int i = 0; i < indent; i++) {
            stringBuilder.append("  ");
        }
    }

    public void initialize() {
        // do nothing
    }

    public Object getDefaultValue() {
        return null;
    }

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(final int indent) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);

        if (MapUtils.isNotEmpty(fields)) {
            for (Map.Entry<String, Field> entry : fields.entrySet()) {
                addIndent(stringBuilder, indent+1);
                stringBuilder.append(entry.getKey()).append(" = ").append(entry.getValue().toString(0));
            }
            stringBuilder.append("\n");
        } 
        // else {
        //     stringBuilder.append("\n");
        // }

        return stringBuilder.toString();
    }

    public void addField(String fieldName, DataType dataType, String initialValue) {
        if (fields.containsKey(fieldName)) {
            throw new RuntimeException("Field \"" + fieldName + "\" already contained!");
        }

        Field field = new Field();
        field.setName(fieldName);
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

    public Map<String, Field> getFields() {
        return fields;
    }
}
