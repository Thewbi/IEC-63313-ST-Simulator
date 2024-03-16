package model;

import org.antlr.v4.runtime.RuntimeMetaData;

public class Configuration {

    private String name;

    private Task task;

    private ProgramConfiguration programConfiguration;

    public void addIndent(StringBuilder stringBuilder, int indent) {
        for (int i = 0; i < indent; i++) {
            stringBuilder.append("  ");
        }
    }

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(final int indent) {

        StringBuilder stringBuilder = new StringBuilder();

        addIndent(stringBuilder, indent);

        stringBuilder.append("Configuration: name: ").append(name).append("\n");
        
        addIndent(stringBuilder, indent+1);
        stringBuilder.append(task);
        stringBuilder.append("\n");

        addIndent(stringBuilder, indent+1);
        stringBuilder.append(programConfiguration);
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public ProgramConfiguration getProgramConfiguration() {
        return programConfiguration;
    }

    public void setProgramConfiguration(ProgramConfiguration programConfiguration) {
        this.programConfiguration = programConfiguration;
    }
    
}
