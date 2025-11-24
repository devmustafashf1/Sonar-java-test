package model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectTest {
    private Project project;
    private Task task1;
    private Task task2;
    private Task task3;
    private Resource resource1;
    private Resource resource2;

    @Before
    public void setUp() {
        project = new Project();
        
        LocalDateTime start1 = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end1 = LocalDateTime.of(2025, 9, 20, 18, 0);
        task1 = new Task(1, "Task 1", start1, end1, new ArrayList<>());
        
        LocalDateTime start2 = LocalDateTime.of(2025, 9, 21, 8, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 9, 22, 18, 0);
        task2 = new Task(2, "Task 2", start2, end2, new ArrayList<>());
        
        LocalDateTime start3 = LocalDateTime.of(2025, 9, 20, 8, 0);
        LocalDateTime end3 = LocalDateTime.of(2025, 9, 21, 18, 0);
        task3 = new Task(3, "Task 3", start3, end3, new ArrayList<>());
        
        resource1 = new Resource("Resource 1");
        resource2 = new Resource("Resource 2");
    }

    @Test
    public void testAddTask() {
        project.addTask(task1);
        List<Task> tasks = project.getTasks();
        assertEquals(1, tasks.size());
        assertTrue(tasks.contains(task1));
    }

    @Test
    public void testAddMultipleTasks() {
        project.addTask(task1);
        project.addTask(task2);
        project.addTask(task3);
        assertEquals(3, project.getTasks().size());
    }

    @Test
    public void testAddResource() {
        project.addResource(resource1);
        List<Resource> resources = project.getResources();
        assertEquals(1, resources.size());
        assertTrue(resources.contains(resource1));
    }

    @Test
    public void testAddMultipleResources() {
        project.addResource(resource1);
        project.addResource(resource2);
        assertEquals(2, project.getResources().size());
    }

    @Test
    public void testGetTasks() {
        assertNotNull(project.getTasks());
        assertTrue(project.getTasks().isEmpty());
        
        project.addTask(task1);
        assertFalse(project.getTasks().isEmpty());
    }

    @Test
    public void testGetResources() {
        assertNotNull(project.getResources());
        assertTrue(project.getResources().isEmpty());
        
        project.addResource(resource1);
        assertFalse(project.getResources().isEmpty());
    }

    @Test
    public void testGetCompletionTimeWithNoTasks() {
        String result = project.getCompletionTime();
        assertEquals("No tasks", result);
    }

    @Test
    public void testGetCompletionTimeWithSingleTask() {
        project.addTask(task1);
        String result = project.getCompletionTime();
        assertNotNull(result);
        assertTrue(result.contains("Project starts:"));
        assertTrue(result.contains("ends:"));
        assertTrue(result.contains("duration:"));
        assertTrue(result.contains(task1.getStart().toString()));
        assertTrue(result.contains(task1.getEnd().toString()));
    }

    @Test
    public void testGetCompletionTimeWithMultipleTasks() {
        project.addTask(task1);
        project.addTask(task2);
        project.addTask(task3);
        
        String result = project.getCompletionTime();
        assertNotNull(result);
        assertTrue(result.contains("Project starts:"));
        assertTrue(result.contains("ends:"));
        assertTrue(result.contains("duration:"));
    }

    @Test
    public void testGetCompletionTimeFindsEarliestStart() {
        LocalDateTime earliest = LocalDateTime.of(2025, 9, 18, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 19, 18, 0);
        Task earliestTask = new Task(10, "Earliest", earliest, end, new ArrayList<>());
        
        project.addTask(task1);
        project.addTask(earliestTask);
        project.addTask(task2);
        
        String result = project.getCompletionTime();
        assertTrue(result.contains(earliest.toString()));
    }

    @Test
    public void testGetCompletionTimeFindsLatestEnd() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 25, 8, 0);
        LocalDateTime latest = LocalDateTime.of(2025, 9, 26, 18, 0);
        Task latestTask = new Task(10, "Latest", start, latest, new ArrayList<>());
        
        project.addTask(task1);
        project.addTask(task2);
        project.addTask(latestTask);
        
        String result = project.getCompletionTime();
        assertTrue(result.contains(latest.toString()));
    }

    @Test
    public void testOverlappingTasksWithNoTasks() {
        List<String> overlaps = project.overlappingTasks();
        assertNotNull(overlaps);
        assertTrue(overlaps.isEmpty());
    }

    @Test
    public void testOverlappingTasksWithSingleTask() {
        project.addTask(task1);
        List<String> overlaps = project.overlappingTasks();
        assertTrue(overlaps.isEmpty());
    }

    @Test
    public void testOverlappingTasksWithNoOverlaps() {
        project.addTask(task1);
        project.addTask(task2);
        List<String> overlaps = project.overlappingTasks();
        assertTrue(overlaps.isEmpty());
    }

    @Test
    public void testOverlappingTasksWithOverlaps() {
        project.addTask(task1);
        project.addTask(task3); // Overlaps with task1
        List<String> overlaps = project.overlappingTasks();
        assertFalse(overlaps.isEmpty());
        assertTrue(overlaps.size() >= 1);
        assertTrue(overlaps.get(0).contains("Tasks"));
        assertTrue(overlaps.get(0).contains("overlap"));
    }

    @Test
    public void testOverlappingTasksMultipleOverlaps() {
        LocalDateTime start4 = LocalDateTime.of(2025, 9, 19, 10, 0);
        LocalDateTime end4 = LocalDateTime.of(2025, 9, 22, 10, 0);
        Task task4 = new Task(4, "Task 4", start4, end4, new ArrayList<>());
        
        project.addTask(task1);
        project.addTask(task2);
        project.addTask(task3);
        project.addTask(task4);
        
        List<String> overlaps = project.overlappingTasks();
        assertFalse(overlaps.isEmpty());
    }

    @Test
    public void testTeamForTaskWithNoResources() {
        project.addTask(task1);
        List<String> team = project.teamForTask(1);
        assertNotNull(team);
        assertTrue(team.isEmpty());
    }

    @Test
    public void testTeamForTaskWithNoMatchingAllocations() {
        project.addTask(task1);
        project.addResource(resource1);
        List<String> team = project.teamForTask(1);
        assertTrue(team.isEmpty());
    }

    @Test
    public void testTeamForTaskWithMatchingAllocation() {
        project.addTask(task1);
        resource1.addAllocation(new Allocation(task1, 50));
        project.addResource(resource1);
        
        List<String> team = project.teamForTask(1);
        assertEquals(1, team.size());
        assertTrue(team.get(0).contains("Resource 1"));
        assertTrue(team.get(0).contains("50%"));
    }

    @Test
    public void testTeamForTaskWithMultipleResources() {
        project.addTask(task1);
        resource1.addAllocation(new Allocation(task1, 50));
        resource2.addAllocation(new Allocation(task1, 100));
        project.addResource(resource1);
        project.addResource(resource2);
        
        List<String> team = project.teamForTask(1);
        assertEquals(2, team.size());
    }

    @Test
    public void testTeamForTaskWithNonExistentTaskId() {
        project.addTask(task1);
        resource1.addAllocation(new Allocation(task1, 50));
        project.addResource(resource1);
        
        List<String> team = project.teamForTask(999);
        assertTrue(team.isEmpty());
    }

    @Test
    public void testEffortPerResourceWithNoResources() {
        List<String> effort = project.effortPerResource();
        assertNotNull(effort);
        assertTrue(effort.isEmpty());
    }

    @Test
    public void testEffortPerResourceWithNoAllocations() {
        project.addResource(resource1);
        List<String> effort = project.effortPerResource();
        assertEquals(1, effort.size());
        assertTrue(effort.get(0).contains("Resource 1"));
        assertTrue(effort.get(0).contains("0.0 hours"));
    }

    @Test
    public void testEffortPerResourceWithSingleAllocation() {
        project.addTask(task1);
        resource1.addAllocation(new Allocation(task1, 50));
        project.addResource(resource1);
        
        List<String> effort = project.effortPerResource();
        assertEquals(1, effort.size());
        assertTrue(effort.get(0).contains("Resource 1"));
        assertTrue(effort.get(0).contains("hours"));
    }

    @Test
    public void testEffortPerResourceWithMultipleAllocations() {
        project.addTask(task1);
        project.addTask(task2);
        resource1.addAllocation(new Allocation(task1, 50));
        resource1.addAllocation(new Allocation(task2, 100));
        project.addResource(resource1);
        
        List<String> effort = project.effortPerResource();
        assertEquals(1, effort.size());
        assertTrue(effort.get(0).contains("Resource 1"));
    }

    @Test
    public void testEffortPerResourceWithMultipleResources() {
        project.addTask(task1);
        resource1.addAllocation(new Allocation(task1, 50));
        resource2.addAllocation(new Allocation(task1, 100));
        project.addResource(resource1);
        project.addResource(resource2);
        
        List<String> effort = project.effortPerResource();
        assertEquals(2, effort.size());
    }

    @Test
    public void testEffortPerResourceCalculation() {
        // Task duration: 34 hours (from 2025-09-19 08:00 to 2025-09-20 18:00)
        // 50% allocation = 17 hours
        project.addTask(task1);
        resource1.addAllocation(new Allocation(task1, 50));
        project.addResource(resource1);
        
        List<String> effort = project.effortPerResource();
        String effortStr = effort.get(0);
        assertTrue(effortStr.contains("Resource 1"));
        assertTrue(effortStr.contains("hours"));
    }
}

