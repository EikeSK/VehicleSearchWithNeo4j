package repositories;


import domain.VehicleNode;
import support.VehicleNodeSearchQuery;

public interface VehicleNodeRepositoryCustom {

    public Iterable<VehicleNode> findNodesByQuery(final VehicleNodeSearchQuery searchQuery);
}
