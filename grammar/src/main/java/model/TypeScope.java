package model;

import java.util.HashMap;
import java.util.Map;

public class TypeScope {

    private Map<String, DataType> typeMap = new HashMap<>();

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, DataType> entry : typeMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" = ").append(entry.getValue()/* .getName() */);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
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
