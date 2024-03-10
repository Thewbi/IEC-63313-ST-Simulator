package model;

public class Field {

    private DataType dataType;

    private String initialValue;

    @Override
    public String toString() {
        return "Field [dataType=" + dataType + ", initialValue=" + initialValue + "]";
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

}
