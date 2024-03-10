package model;

public class ParameterAssignment {

    private String parameterName;

    private String value;

    @Override
    public String toString() {
        return "ParameterAssignment [parameterName=" + parameterName + ", value=" + value + "]";
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
