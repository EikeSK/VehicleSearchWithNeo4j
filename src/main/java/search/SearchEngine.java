package search;

import domain.VehicleNode;

import java.util.Collection;

public interface SearchEngine {

    public Collection<VehicleNode> search(String searchString);
}
