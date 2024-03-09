package model;

public class Variable {

    private String name;

    private DataType dataType = DataType.UNKNOWN;

    @Override
    public String toString() {
        return "Variable [name=" + name + ", dataType=" + dataType + "]";
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
    
}
