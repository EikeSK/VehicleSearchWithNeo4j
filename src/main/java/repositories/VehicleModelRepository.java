package repositories;

import domain.VehicleModel;
import org.springframework.data.repository.CrudRepository;

public interface VehicleModelRepository extends CrudRepository<VehicleModel, String>, VehicleModelRepositoryCustom {

    public Iterable<VehicleModel> findByName(final String name);
}
