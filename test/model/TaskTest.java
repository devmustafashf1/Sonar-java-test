package model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskTest {
    private Task task;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Integer> dependencies;

    @Before
    public void setUp() {
        start = LocalDateTime.of(2025, 9, 19, 8, 0);
        end = LocalDateTime.of(2025, 9, 20, 18, 0);
        dependencies = new ArrayList<>();
        dependencies.add(1);
        dependencies.add(2);
        task = new Task(1, "Test Task", start, end, dependencies);
    }

    @Test
    public void testConstructor() {
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals(start, task.getStart());
        assertEquals(end, task.getEnd());
        assertEquals(dependencies, task.getDependencies());
    }

    @Test
    public void testConstructorWithEmptyDependencies() {
        List<Integer> emptyDeps = new ArrayList<>();
        Task taskNoDeps = new Task(2, "No Dependencies", start, end, emptyDeps);
        assertNotNull(taskNoDeps);
        assertTrue(taskNoDeps.getDependencies().isEmpty());
    }

    @Test
    public void testGetId() {
        assertEquals(1, task.getId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Test Task", task.getTitle());
    }

    @Test
    public void testGetStart() {
        assertEquals(start, task.getStart());
    }

    @Test
    public void testGetEnd() {
        assertEquals(end, task.getEnd());
    }

    @Test
    public void testGetDependencies() {
        assertEquals(2, task.getDependencies().size());
        assertTrue(task.getDependencies().contains(1));
        assertTrue(task.getDependencies().contains(2));
    }

    @Test
    public void testGetDurationHours() {
        long expectedHours = 34; // 1 day 10 hours
        assertEquals(expectedHours, task.getDurationHours());
    }

    @Test
    public void testGetDurationHoursSingleHour() {
        LocalDateTime start2 = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 9, 19, 9, 0);
        Task shortTask = new Task(3, "Short Task", start2, end2, new ArrayList<>());
        assertEquals(1, shortTask.getDurationHours());
    }

    @Test
    public void testGetDurationHoursZero() {
        LocalDateTime sameTime = LocalDateTime.of(2025, 9, 19, 8, 0);
        Task zeroTask = new Task(4, "Zero Task", sameTime, sameTime, new ArrayList<>());
        assertEquals(0, zeroTask.getDurationHours());
    }

    @Test
    public void testToString() {
        String result = task.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("Test Task"));
        assertTrue(result.contains(start.toString()));
        assertTrue(result.contains(end.toString()));
    }
}

