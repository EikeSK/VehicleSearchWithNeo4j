package repositories;


import domain.VehicleNode;
import support.VehicleNodeSearchQuery;

/**
 * Repräsentiert eine Erweiterung des Repositoies zum Zugriff auf die Datenbank für Objekte vom Typ VehicleNode.
 */
public interface VehicleNodeRepositoryCustom {

    /**
     * Sucht Fahrzeugknoten anhand einer Suchanfrage vom Typ VehicleNodeSearchQuery
     *
     * @param searchQuery die Suchanfrage in Form eines Objektes vom Typ VehicleNodeSearchQuery
     * @return ein Iterable, welches die gefundenen Fahrzeugknoten beinhaltet, sofern passende gefunden wurden.
     */
    public Iterable<VehicleNode> findNodesByQuery(final VehicleNodeSearchQuery searchQuery);
}
