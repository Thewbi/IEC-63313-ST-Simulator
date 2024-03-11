package model;

import java.util.HashMap;
import java.util.Map;

public class TypeScope {

    private Map<String, DataType> typeMap = new HashMap<>();

    // private TypeScope parentTypeScope;

    @Override
    public String toString() {
        //return "TypeScope [typeMap=" + typeMap + "]";

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, DataType> entry : typeMap.entrySet())
        {
            stringBuilder.append(entry.getKey()).append(" = ").append(entry.getValue()/*.getName() */);
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

    public DataType retrieveDataTypeByTypeName(String dataTypeAsString) {

        if (typeMap.containsKey(dataTypeAsString)) {
            return typeMap.get(dataTypeAsString);
        }

        return null;

        // if (parentTypeScope == null) {
        //     return null;
        // }

        // return parentTypeScope.retrieveDataTypeByTypeName(dataTypeAsString);
    }

    // public TypeScope getParentTypeScope() {
    //     return parentTypeScope;
    // }

    // public void setParentTypeScope(TypeScope parentTypeScope) {
    //     this.parentTypeScope = parentTypeScope;
    // }
}
