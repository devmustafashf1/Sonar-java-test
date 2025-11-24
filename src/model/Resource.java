package model;

import java.util.ArrayList;
import java.util.List;

public class Resource {
    private String name;
    private List<Allocation> allocations = new ArrayList<>();

    public Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addAllocation(Allocation allocation) {
        allocations.add(allocation);
    }

    public List<Allocation> getAllocations() {
        return allocations;
    }
}
