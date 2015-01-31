package repositories;

import domain.VehicleNode;
import org.springframework.data.repository.CrudRepository;


/**
 * Repräsentiert das Repository zum Zugriff auf die Datenbank mit Objekten vom Typ VehicleNode (Fahrzeugknoten).
 */
public interface VehicleNodeRepository extends CrudRepository<VehicleNode, String>, VehicleNodeRepositoryCustom {

}
