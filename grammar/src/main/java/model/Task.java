package model;

public class Task {

    private String name;

    private int priority = -1;

    private String interval;

    // @Override
    // public String toString() {
    //     StringBuilder stringBuilder = new StringBuilder();
    //     stringBuilder.append("Task: name: ").append(name);
    //     stringBuilder.append("\npriority: ").append(priority);
    //     stringBuilder.append("\ninterval: ").append(interval);
    //     return stringBuilder.toString();
    // }

    @Override
    public String toString() {
        return "Task [name=" + name + ", priority=" + priority + ", interval=" + interval + "]";
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
