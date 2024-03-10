package model;

import java.util.HashMap;
import java.util.Map;

public class DataType {
    
    private String name;

    private Map<String, DataType> fields = new HashMap<>();

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
