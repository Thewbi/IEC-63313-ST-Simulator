package model;

public class Variable {

    private String name;

    private DataType dataType;

    private String initialValue;

    private boolean external;

    @Override
    public String toString() {
        return "Variable [name=" + name + ", dataType=" + dataType + ", initialValue=" + initialValue + "]";
    }

    public Object toShortString() {
        return "Variable [name=" + name + ", dataType=" + dataType.getName() + ", initialValue=" + initialValue + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }
    
}
