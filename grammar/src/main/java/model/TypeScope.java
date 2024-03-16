package model;

import java.util.HashMap;
import java.util.Map;

public class TypeScope {

    private Map<String, DataType> typeMap = new HashMap<>();

    public void initialize() {
        for (DataType dataType : typeMap.values()) {
            dataType.initialize();
        }
    }

    @Override
    public String toString() {
        throw new RuntimeException("");
    }

    public String toString(final int indent) {

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, DataType> entry : typeMap.entrySet()) {

            addIndent(stringBuilder, indent);
            stringBuilder.append(entry.getKey()).append(" = ");

            DataType value = entry.getValue();
            stringBuilder.append(value.toString(indent+1));

            stringBuilder.append("\n\n");
        }

        return stringBuilder.toString();
    }

    public void addIndent(StringBuilder stringBuilder, int indent) {
        for (int i = 0; i < indent; i++) {
            stringBuilder.append("  ");
        }
    }

    public void addType(String typeName, DataType object) {
        if (typeMap.containsKey(typeName)) {
            throw new RuntimeException("Type \"" + typeName + "\" already defined!");
        }
        typeMap.put(typeName, object);
    }

    public DataType retrieveDataTypeByTypeName(String dataTypeName) {
        if (typeMap.containsKey(dataTypeName)) {
            return typeMap.get(dataTypeName);
        }
        return null;
    }

    public DataType get(String dataTypeName) {
        return retrieveDataTypeByTypeName(dataTypeName);
    }
}
