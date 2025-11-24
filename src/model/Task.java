package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Task {
    private int id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Integer> dependencies;  

    public Task(int id, String title, LocalDateTime start, LocalDateTime end, List<Integer> dependencies) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.dependencies = dependencies;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public List<Integer> getDependencies() { return dependencies; }

    public long getDurationHours() {
        return Duration.between(start, end).toHours();
    }

    public String toString() {
        return id + ": " + title + " (" + start + " → " + end + ")";
    }
}
