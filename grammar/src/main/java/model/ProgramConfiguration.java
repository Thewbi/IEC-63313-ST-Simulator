package model;

public class ProgramConfiguration {

    private String programName;

    private String taskName;

    private String programType;

    @Override
    public String toString() {
        return "ProgramConfiguration [programName=" + programName + ", taskName=" + taskName + ", programType="
                + programType + "]";
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }
    
}
