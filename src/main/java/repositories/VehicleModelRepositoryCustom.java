package repositories;


import domain.VehicleNode;

public interface VehicleModelRepositoryCustom {

    public Iterable<VehicleNode> findModelsByQuery(final String query);
}
