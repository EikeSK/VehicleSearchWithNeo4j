package repositories;


import domain.VehicleModel;

public interface VehicleModelRepositoryCustom {

    public Iterable<VehicleModel> findModelsByQuery(final String query);
}
