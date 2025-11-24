package model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResourceTest {
    private Resource resource;
    private Task task1;
    private Task task2;
    private Allocation allocation1;
    private Allocation allocation2;

    @Before
    public void setUp() {
        resource = new Resource("John Doe");
        
        LocalDateTime start1 = LocalDateTime.of(2025, 9, 19, 8, 0);
        LocalDateTime end1 = LocalDateTime.of(2025, 9, 20, 18, 0);
        task1 = new Task(1, "Task 1", start1, end1, new ArrayList<>());
        
        LocalDateTime start2 = LocalDateTime.of(2025, 9, 21, 8, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 9, 22, 18, 0);
        task2 = new Task(2, "Task 2", start2, end2, new ArrayList<>());
        
        allocation1 = new Allocation(task1, 50);
        allocation2 = new Allocation(task2, 100);
    }

    @Test
    public void testConstructor() {
        assertNotNull(resource);
        assertEquals("John Doe", resource.getName());
        assertTrue(resource.getAllocations().isEmpty());
    }

    @Test
    public void testGetName() {
        assertEquals("John Doe", resource.getName());
    }

    @Test
    public void testGetNameWithDifferentName() {
        Resource resource2 = new Resource("Jane Smith");
        assertEquals("Jane Smith", resource2.getName());
    }

    @Test
    public void testAddAllocation() {
        resource.addAllocation(allocation1);
        assertEquals(1, resource.getAllocations().size());
        assertEquals(allocation1, resource.getAllocations().get(0));
    }

    @Test
    public void testAddMultipleAllocations() {
        resource.addAllocation(allocation1);
        resource.addAllocation(allocation2);
        assertEquals(2, resource.getAllocations().size());
        assertTrue(resource.getAllocations().contains(allocation1));
        assertTrue(resource.getAllocations().contains(allocation2));
    }

    @Test
    public void testGetAllocationsEmpty() {
        List<Allocation> allocations = resource.getAllocations();
        assertNotNull(allocations);
        assertTrue(allocations.isEmpty());
    }

    @Test
    public void testGetAllocationsAfterAdding() {
        resource.addAllocation(allocation1);
        resource.addAllocation(allocation2);
        List<Allocation> allocations = resource.getAllocations();
        assertEquals(2, allocations.size());
    }
}

