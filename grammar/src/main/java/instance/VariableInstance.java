package instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DataType;
import model.Statement;

public class VariableInstance {

    private String name;

    private DataType dataType;

    private String value;

    private Map<String, VariableDescriptor> elements = new HashMap<>();

    private List<Statement> statements = new ArrayList<>();

    public void addElement(VariableInstance element) {
        boolean retain = false;
        boolean external = false;
        addElement(element, retain, external);
    }

    public void addElement(VariableInstance element, boolean retain, boolean external) {
        if (elements.containsKey(element.getName())) {
            throw new RuntimeException("Element " + element.getName() + " contained already!");
        }

        VariableDescriptor variableDescriptor = new VariableDescriptor();
        variableDescriptor.variableInstance = element;
        variableDescriptor.retain = retain;
        variableDescriptor.external = external;

        elements.put(element.getName(), variableDescriptor);
    }

    public void setElement(String parameterName, VariableInstance variableInstance) {

        VariableDescriptor variableDescriptor = new VariableDescriptor();
        variableDescriptor.variableInstance = variableInstance;
        variableDescriptor.retain = false;
        variableDescriptor.external = false;

        elements.put(parameterName, variableDescriptor);
    }

    public VariableInstance getElement(String name) {
        if (!elements.containsKey(name)) {
            return null;
        }
        return elements.get(name).variableInstance;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Map<String, VariableDescriptor> getElements() {
        return elements;
    }
    
}
