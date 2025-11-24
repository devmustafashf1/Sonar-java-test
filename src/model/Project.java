package model;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private List<Task> tasks = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Resource> getResources() {
        return resources;
    }

    // i) Find project completion time and duration
    public String getCompletionTime() {
        if (tasks.isEmpty()) return "No tasks";

        Task first = tasks.get(0);
        Task last = tasks.get(0);

        for (Task t : tasks) {
            if (t.getStart().isBefore(first.getStart())) {
                first = t;
            }
            if (t.getEnd().isAfter(last.getEnd())) {
                last = t;
            }
        }

        long hours = java.time.Duration.between(first.getStart(), last.getEnd()).toHours();
        return "Project starts: " + first.getStart() +
               ", ends: " + last.getEnd() +
               ", duration: " + hours + " hours";
    }

    // ii) Highlight overlapping tasks
    public List<String> overlappingTasks() {
        List<String> overlaps = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task t1 = tasks.get(i);
            for (int j = i + 1; j < tasks.size(); j++) {
                Task t2 = tasks.get(j);
                boolean overlap = t1.getStart().isBefore(t2.getEnd())
                        && t2.getStart().isBefore(t1.getEnd());
                if (overlap) {
                    overlaps.add("Tasks " + t1.getId() + " and " + t2.getId() + " overlap");
                }
            }
        }
        return overlaps;
    }

   
    public List<String> teamForTask(int taskId) {
        List<String> team = new ArrayList<>();
        for (Resource r : resources) {
            for (Allocation a : r.getAllocations()) {
                if (a.getTask().getId() == taskId) {
                    team.add(r.getName() + " (" + a.getPercentage() + "%)");
                }
            }
        }
        return team;
    }

    
    public List<String> effortPerResource() {
        List<String> effortList = new ArrayList<>();
        for (Resource r : resources) {
            double totalHours = 0;
            for (Allocation a : r.getAllocations()) {
                long hours = a.getTask().getDurationHours();
                totalHours += (hours * a.getPercentage()) / 100.0;
            }
            effortList.add(r.getName() + ": " + totalHours + " hours");
        }
        return effortList;
    }
}
