package util;

import model.Task;
import model.Resource;
import model.Allocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResourceParser {

    public static List<Resource> parseResources(String filename, List<Task> tasks) throws IOException {
        List<Resource> resources = new ArrayList<>();

        // Read all lines from file
        List<String> lines = Files.readAllLines(Paths.get(filename));

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            // Format: ResourceName,taskId:percent,taskId:percent,...
            String[] parts = line.split(",");
            String resourceName = parts[0].trim();

            Resource r = new Resource(resourceName);

            for (int i = 1; i < parts.length; i++) {
                String[] allocParts = parts[i].split(":");
                int taskId = Integer.parseInt(allocParts[0].trim());
                int percent = Integer.parseInt(allocParts[1].trim());

                // Find matching task by ID
                Task task = tasks.stream()
                                 .filter(t -> t.getId() == taskId)
                                 .findFirst()
                                 .orElse(null);

                if (task != null) {
                    r.addAllocation(new Allocation(task, percent));
                }
            }

            resources.add(r);
        }

        return resources;
    }
}
