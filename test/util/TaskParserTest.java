package util;

import model.Task;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class TaskParserTest {
    private static final String TEST_FILE = "test_tasks.txt";

    @Test
    public void testParseTasksWithValidFile() throws IOException {
        // Create test file
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("1, Initial research and analysis, 20250919+0800, 20250920+1800,\n");
            writer.write("2, Develop program content, 20250919+0900, 20250925+1700, 1\n");
        }

        List<Task> tasks = TaskParser.parseTasks(TEST_FILE);
        
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        
        Task task1 = tasks.get(0);
        assertEquals(1, task1.getId());
        assertEquals("Initial research and analysis", task1.getTitle());
        assertEquals(LocalDateTime.of(2025, 9, 19, 8, 0), task1.getStart());
        assertEquals(LocalDateTime.of(2025, 9, 20, 18, 0), task1.getEnd());
        assertTrue(task1.getDependencies().isEmpty());
        
        Task task2 = tasks.get(1);
        assertEquals(2, task2.getId());
        assertEquals("Develop program content", task2.getTitle());
        assertEquals(1, task2.getDependencies().size());
        assertEquals(Integer.valueOf(1), task2.getDependencies().get(0));
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseTasksWithEmptyFile() throws IOException {
        // Create empty test file
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("");
        }

        List<Task> tasks = TaskParser.parseTasks(TEST_FILE);
        
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseTasksWithEmptyLines() throws IOException {
        // Create test file with empty lines
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("1, Task 1, 20250919+0800, 20250920+1800,\n");
            writer.write("\n");
            writer.write("2, Task 2, 20250921+0800, 20250922+1800,\n");
            writer.write("\n");
        }

        List<Task> tasks = TaskParser.parseTasks(TEST_FILE);
        
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseTasksWithMultipleDependencies() throws IOException {
        // Create test file with multiple dependencies
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("5, Task 5, 20251001+0900, 20251005+1700, 2, 4\n");
        }

        List<Task> tasks = TaskParser.parseTasks(TEST_FILE);
        
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        
        Task task = tasks.get(0);
        assertEquals(5, task.getId());
        assertEquals(2, task.getDependencies().size());
        assertTrue(task.getDependencies().contains(2));
        assertTrue(task.getDependencies().contains(4));
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseTasksWithWhitespace() throws IOException {
        // Create test file with extra whitespace
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("  1  ,  Task with spaces  ,  20250919+0800  ,  20250920+1800  ,  \n");
        }

        List<Task> tasks = TaskParser.parseTasks(TEST_FILE);
        
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        
        Task task = tasks.get(0);
        assertEquals(1, task.getId());
        assertEquals("Task with spaces", task.getTitle());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseTasksWithNoDependencies() throws IOException {
        // Create test file with no dependencies
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("1, Task 1, 20250919+0800, 20250920+1800,\n");
        }

        List<Task> tasks = TaskParser.parseTasks(TEST_FILE);
        
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0).getDependencies().isEmpty());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseTasksWithSingleDependency() throws IOException {
        // Create test file with single dependency
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("2, Task 2, 20250919+0900, 20250925+1700, 1\n");
        }

        List<Task> tasks = TaskParser.parseTasks(TEST_FILE);
        
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(1, tasks.get(0).getDependencies().size());
        assertEquals(Integer.valueOf(1), tasks.get(0).getDependencies().get(0));
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseTasksDateTimeFormat() throws IOException {
        // Test various date-time formats
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("1, Task 1, 20250919+0800, 20250920+1800,\n");
            writer.write("2, Task 2, 20251001+0900, 20251005+1700, 1\n");
        }

        List<Task> tasks = TaskParser.parseTasks(TEST_FILE);
        
        assertEquals(2, tasks.size());
        assertEquals(LocalDateTime.of(2025, 9, 19, 8, 0), tasks.get(0).getStart());
        assertEquals(LocalDateTime.of(2025, 10, 1, 9, 0), tasks.get(1).getStart());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }
}

