package model;

public class Allocation {
    private Task task;
    private int percentage;

    public Allocation(Task task, int percentage) {
        this.task = task;
        this.percentage = percentage;
    }

    public Task getTask() {
        return task;
    }

    public int getPercentage() {
        return percentage;
    }
}
