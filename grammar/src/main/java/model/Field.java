package model;

public class Field {

    private String name;

    private DataType dataType;

    private String initialValue;

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(final int indent) {
        return "Field [dataType=" + dataType.toString(indent) + ", initialValue=" + initialValue + "]";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
