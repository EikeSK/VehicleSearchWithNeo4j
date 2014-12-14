package repositories;

import domain.VehicleNode;
import org.springframework.data.repository.CrudRepository;

public interface VehicleNodeRepository extends CrudRepository<VehicleNode, String>, VehicleNodeRepositoryCustom {

    public Iterable<VehicleNode> findByName(final String name);
}
