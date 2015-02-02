package service;


import domain.VehicleNode;
import support.VehicleNodeSearchQuery;

import java.util.List;

/**
 * Repräsentiert eine Serviceklasse zum Zugriff auf die Datenbank mit Objekte vom Typ VehicleNode
 */
public interface VehicleNodeService {

    /**
     * Liefert anhand eines VehicleNodeSearchQuery eine Liste von passenden Fahrzeugknoten (VehicleNode).
     * Jeder Aufruf dieser Methode wird dabei in einer Transaktion ausgeführt.
     *
     * @param searchQuery Die Suchanfrage an die Datenbank in Form eines Objektes vom Typ VehicleNodeSearchQuery
     * @return Eine Liste mit passenden Fahrzeugknoten, die anhand der Suchanfrage ermittelt wurden.
     */
    public List<VehicleNode> findNodesByQuery(final VehicleNodeSearchQuery searchQuery);
}
