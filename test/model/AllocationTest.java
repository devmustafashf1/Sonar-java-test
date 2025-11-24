package model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AllocationTest {
    private Task task;
    private Allocation allocation;

    @Test
    public void testConstructor() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 20, 18, 0);
        task = new Task(1, "Test Task", start, end, new ArrayList<>());
        allocation = new Allocation(task, 75);
        
        assertNotNull(allocation);
        assertEquals(task, allocation.getTask());
        assertEquals(75, allocation.getPercentage());
    }

    @Test
    public void testGetTask() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 20, 18, 0);
        task = new Task(1, "Test Task", start, end, new ArrayList<>());
        allocation = new Allocation(task, 50);
        
        Task retrievedTask = allocation.getTask();
        assertNotNull(retrievedTask);
        assertEquals(task, retrievedTask);
        assertEquals(1, retrievedTask.getId());
        assertEquals("Test Task", retrievedTask.getTitle());
    }

    @Test
    public void testGetPercentage() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 20, 18, 0);
        task = new Task(1, "Test Task", start, end, new ArrayList<>());
        
        Allocation alloc1 = new Allocation(task, 0);
        assertEquals(0, alloc1.getPercentage());
        
        Allocation alloc2 = new Allocation(task, 50);
        assertEquals(50, alloc2.getPercentage());
        
        Allocation alloc3 = new Allocation(task, 100);
        assertEquals(100, alloc3.getPercentage());
        
        Allocation alloc4 = new Allocation(task, 150);
        assertEquals(150, alloc4.getPercentage());
    }

    @Test
    public void testMultipleAllocationsWithSameTask() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 20, 18, 0);
        task = new Task(1, "Test Task", start, end, new ArrayList<>());
        
        Allocation alloc1 = new Allocation(task, 50);
        Allocation alloc2 = new Allocation(task, 75);
        
        assertEquals(task, alloc1.getTask());
        assertEquals(task, alloc2.getTask());
        assertEquals(50, alloc1.getPercentage());
        assertEquals(75, alloc2.getPercentage());
    }
}

