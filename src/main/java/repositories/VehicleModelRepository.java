package repositories;

import domain.VehicleModel;
import org.springframework.data.repository.CrudRepository;

public interface VehicleModelRepository extends CrudRepository<VehicleModel, String> {

    public Iterable<VehicleModel> findByName(final String name);
}
