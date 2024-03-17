package model;

public class Variable {

    private String name;

    private DataType dataType;

    private String initialValue;

    private boolean external;

    private boolean inOut;

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(final int indent) {

        StringBuilder stringBuilder = new StringBuilder();
        DataType.addIndent(stringBuilder, indent);

        stringBuilder.append("Variable [name=" + name + ", dataType=" + dataType.toString(indent + 1) + ", initialValue="
                + initialValue + ", inOut: "
                + inOut + "]");

        return stringBuilder.toString();
    }

    public Object toShortString() {
        return "Variable [name=" + name + ", dataType=" + dataType.getName() + ", initialValue=" + initialValue
                + " inOut: " + inOut + "]";
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

    public boolean isInOut() {
        return inOut;
    }

    public void setInOut(boolean inOut) {
        this.inOut = inOut;
    }

}
