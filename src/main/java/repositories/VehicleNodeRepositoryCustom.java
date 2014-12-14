package repositories;


import domain.VehicleNode;

public interface VehicleNodeRepositoryCustom {

    public Iterable<VehicleNode> findModelsByQuery(final String query);
}
