package service;

import domain.VehicleNode;
import support.VehicleMetaData;

import java.util.Map;

/**
 * Repräsentiert eine Serviceschnittstelle zum Speichern von Fahrzeugdaten in der Datenbank für die Suche.
 */
public interface VehicleDataPersistenceService {

    /**
     * Speichert ein Fahrzeug als Objekt vom Typ VehicleNode in der Datenbank. Begriffe im Namen des Fahrzeugs,
     * die durch Leerzeichen voneinander getrennt sind, stehen dabei als einzelne Suchbegriffe in der Datenbank zur Verfügung.
     * Anhand dieser Begriffe kann das Fahrzeug gefunden werden.
     * Bereits in der Datenbank vorhandene Suchbegriffe werden um das Fahrzeug erweitert.
     *
     * @param vehicleNode das zu speichernde Fahrzeug in Form einer VehicleNode
     */
    public void save(VehicleNode vehicleNode);

    /**
     * Speichert ein Fahrzeug als Objekt vom Typ VehicleNode mit zusätzlichen Metainformationen.
     * Diese können aus weiteren Suchbegriffe für das Fahrzeug sowie einen Bauzeitraum für das Fahrzeug bestehen.
     * Wurde ein vollständiger Bauzeitraum angegeben, werden die angegebenen sowie die zwischen dem ersten und dem letzten
     * Baujahr liegenden Jahre als vergleichbare Werte in die Datenbank gespeichert.
     *
     * @param vehicleNode        das zu speichernde Fahrzeug in Form einer VehicleNode
     * @param additionalMetaData optional zusätzliche Metainformationen, die zum Fahrzeug gespeichert werden.
     */
    public void save(VehicleNode vehicleNode, VehicleMetaData additionalMetaData);

    /**
     * Wie <code>save(VehicleNode vehicleNode, VehicleMetaData additionalMetaData)</code>, jedoch zum Speichern mehrere Fahrzeuge
     * geeignet.
     *
     * @param batchData Datensatz mit mehreren Fahrzeugen und optionalen Metainformationen
     */
    public void saveBatch(final Map<VehicleNode, VehicleMetaData> batchData);
}
