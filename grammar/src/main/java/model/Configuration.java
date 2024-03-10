package model;

public class Configuration {

    private String name;

    private Task task;

    private ProgramConfiguration programConfiguration;

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Configuration: name: ").append(name);
        stringBuilder.append("\n").append(task);
        stringBuilder.append("\n").append(programConfiguration);

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
