package studenttaskmanager;

public class Task {
    private final int id;
    private final String name;
    private volatile boolean completed;

    public Task(int id, String name) {
        this.id = id;
        this.name = name;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        completed = true;
    }

    @Override
    public String toString() {
        String status;

        if (completed) {
            status = "Completed";
        } else {
            status = "Pending";
        }

        return "Task ID: " + id
                + " | Description: " + name
                + " | Status: " + status;
    }
}