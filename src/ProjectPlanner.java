import model.*;
import util.*;

import java.io.IOException;
import java.util.List;

public class ProjectPlanner {
    public static void main(String[] args) throws IOException {
        Project project = new Project();

        // Load tasks and resources from text files
        List<Task> tasks = TaskParser.parseTasks("tasks.txt");
        for (Task t : tasks) {
            project.addTask(t);
        }

        List<Resource> resources = ResourceParser.parseResources("resources.txt", tasks);
        for (Resource r : resources) {
            project.addResource(r);
        }

        // i) Project completion time
        System.out.println(project.getCompletionTime());

        // ii) Overlapping tasks
        System.out.println("\nOverlapping tasks:");
        for (String s : project.overlappingTasks()) {
            System.out.println(s);
        }

       System.out.println("\nTeams for all tasks:");
    for (Task task : tasks) {
    System.out.println("Task " + task.getId() + " (" + task.getTitle() + "):");
    List<String> team = project.teamForTask(task.getId());
    if (team.isEmpty()) {
        System.out.println("  No resources assigned.");
    } else {
        for (String member : team) {
            System.out.println("  " + member);
        }
    }
    System.out.println();
}


        // iv) Effort per resource
        System.out.println("\nEffort per resource:");
        for (String s : project.effortPerResource()) {
            System.out.println(s);
        }
    }
}
