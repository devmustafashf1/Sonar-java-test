package util;

import model.Task;
import model.Resource;
import model.Allocation;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResourceParserTest {
    private static final String TEST_FILE = "test_resources.txt";

    @Test
    public void testParseResourcesWithValidFile() throws IOException {
        // Create test tasks
        List<Task> tasks = new ArrayList<>();
        LocalDateTime start1 = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end1 = LocalDateTime.of(2025, 9, 20, 18, 0);
        tasks.add(new Task(1, "Task 1", start1, end1, new ArrayList<>()));
        
        LocalDateTime start2 = LocalDateTime.of(2025, 9, 21, 8, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 9, 22, 18, 0);
        tasks.add(new Task(2, "Task 2", start2, end2, new ArrayList<>()));

        // Create test file
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("Ahmed, 1:150, 2:100\n");
            writer.write("Ayesha, 1:50, 2:50\n");
        }

        List<Resource> resources = ResourceParser.parseResources(TEST_FILE, tasks);
        
        assertNotNull(resources);
        assertEquals(2, resources.size());
        
        Resource resource1 = resources.get(0);
        assertEquals("Ahmed", resource1.getName());
        assertEquals(2, resource1.getAllocations().size());
        
        Resource resource2 = resources.get(1);
        assertEquals("Ayesha", resource2.getName());
        assertEquals(2, resource2.getAllocations().size());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseResourcesWithEmptyFile() throws IOException {
        List<Task> tasks = new ArrayList<>();
        
        // Create empty test file
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("");
        }

        List<Resource> resources = ResourceParser.parseResources(TEST_FILE, tasks);
        
        assertNotNull(resources);
        assertTrue(resources.isEmpty());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseResourcesWithEmptyLines() throws IOException {
        List<Task> tasks = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 20, 18, 0);
        tasks.add(new Task(1, "Task 1", start, end, new ArrayList<>()));

        // Create test file with empty lines
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("Ahmed, 1:150\n");
            writer.write("\n");
            writer.write("Ayesha, 1:50\n");
            writer.write("\n");
        }

        List<Resource> resources = ResourceParser.parseResources(TEST_FILE, tasks);
        
        assertNotNull(resources);
        assertEquals(2, resources.size());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseResourcesWithNoAllocations() throws IOException {
        List<Task> tasks = new ArrayList<>();

        // Create test file with resource but no allocations
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("Ahmed\n");
        }

        List<Resource> resources = ResourceParser.parseResources(TEST_FILE, tasks);
        
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("Ahmed", resources.get(0).getName());
        assertTrue(resources.get(0).getAllocations().isEmpty());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseResourcesWithSingleAllocation() throws IOException {
        List<Task> tasks = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 20, 18, 0);
        tasks.add(new Task(1, "Task 1", start, end, new ArrayList<>()));

        // Create test file
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("Ahmed, 1:150\n");
        }

        List<Resource> resources = ResourceParser.parseResources(TEST_FILE, tasks);
        
        assertEquals(1, resources.size());
        Resource resource = resources.get(0);
        assertEquals("Ahmed", resource.getName());
        assertEquals(1, resource.getAllocations().size());
        
        Allocation allocation = resource.getAllocations().get(0);
        assertEquals(1, allocation.getTask().getId());
        assertEquals(150, allocation.getPercentage());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseResourcesWithMultipleAllocations() throws IOException {
        List<Task> tasks = new ArrayList<>();
        LocalDateTime start1 = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end1 = LocalDateTime.of(2025, 9, 20, 18, 0);
        tasks.add(new Task(1, "Task 1", start1, end1, new ArrayList<>()));
        
        LocalDateTime start2 = LocalDateTime.of(2025, 9, 21, 8, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 9, 22, 18, 0);
        tasks.add(new Task(2, "Task 2", start2, end2, new ArrayList<>()));
        
        LocalDateTime start3 = LocalDateTime.of(2025, 9, 23, 8, 0);
        LocalDateTime end3 = LocalDateTime.of(2025, 9, 24, 18, 0);
        tasks.add(new Task(3, "Task 3", start3, end3, new ArrayList<>()));

        // Create test file
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("Ahmed, 1:150, 2:100, 3:50\n");
        }

        List<Resource> resources = ResourceParser.parseResources(TEST_FILE, tasks);
        
        assertEquals(1, resources.size());
        Resource resource = resources.get(0);
        assertEquals(3, resource.getAllocations().size());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseResourcesWithNonExistentTaskId() throws IOException {
        List<Task> tasks = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 20, 18, 0);
        tasks.add(new Task(1, "Task 1", start, end, new ArrayList<>()));

        // Create test file with non-existent task ID
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("Ahmed, 1:150, 999:100\n");
        }

        List<Resource> resources = ResourceParser.parseResources(TEST_FILE, tasks);
        
        assertEquals(1, resources.size());
        Resource resource = resources.get(0);
        // Only task 1 should be allocated, task 999 doesn't exist
        assertEquals(1, resource.getAllocations().size());
        assertEquals(1, resource.getAllocations().get(0).getTask().getId());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseResourcesWithWhitespace() throws IOException {
        List<Task> tasks = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 20, 18, 0);
        tasks.add(new Task(1, "Task 1", start, end, new ArrayList<>()));

        // Create test file with extra whitespace
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("  Ahmed  ,  1  :  150  ,  2  :  100  \n");
        }

        List<Resource> resources = ResourceParser.parseResources(TEST_FILE, tasks);
        
        assertEquals(1, resources.size());
        assertEquals("Ahmed", resources.get(0).getName());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testParseResourcesPercentageValues() throws IOException {
        List<Task> tasks = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 20, 18, 0);
        tasks.add(new Task(1, "Task 1", start, end, new ArrayList<>()));

        // Create test file with various percentage values
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("Ahmed, 1:0\n");
            writer.write("Ayesha, 1:50\n");
            writer.write("Mariyam, 1:100\n");
            writer.write("Bilal, 1:150\n");
        }

        List<Resource> resources = ResourceParser.parseResources(TEST_FILE, tasks);
        
        assertEquals(4, resources.size());
        assertEquals(0, resources.get(0).getAllocations().get(0).getPercentage());
        assertEquals(50, resources.get(1).getAllocations().get(0).getPercentage());
        assertEquals(100, resources.get(2).getAllocations().get(0).getPercentage());
        assertEquals(150, resources.get(3).getAllocations().get(0).getPercentage());
        
        // Clean up
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }
}

