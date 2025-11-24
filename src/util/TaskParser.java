package util;

import model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskParser {
    public static List<Task> parseTasks(String filename) {
        List<Task> tasks = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd+HHmm");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String title = parts[1].trim();
                LocalDateTime start = LocalDateTime.parse(parts[2].trim(), fmt);
                LocalDateTime end = LocalDateTime.parse(parts[3].trim(), fmt);

                List<Integer> deps = new ArrayList<>();
                for (int i = 4; i < parts.length; i++) {
                    if (!parts[i].trim().isEmpty()) {
                        deps.add(Integer.parseInt(parts[i].trim()));
                    }
                }

                tasks.add(new Task(id, title, start, end, deps));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
