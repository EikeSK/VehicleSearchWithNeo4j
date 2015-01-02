package repositories;


import domain.VehicleNode;
import support.VehicleNodeSearchQuery;

import java.util.List;

public interface VehicleNodeService {

    public List<VehicleNode> findNodesByQuery(final VehicleNodeSearchQuery searchQuery);
}
