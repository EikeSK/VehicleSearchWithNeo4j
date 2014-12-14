package repositories;

import domain.VehicleNode;
import org.springframework.data.repository.CrudRepository;

public interface VehicleModelRepository extends CrudRepository<VehicleNode, String>, VehicleModelRepositoryCustom {

    public Iterable<VehicleNode> findByName(final String name);
}
