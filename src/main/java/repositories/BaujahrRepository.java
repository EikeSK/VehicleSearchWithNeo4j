package repositories;

import domain.Baujahr;
import org.springframework.data.repository.CrudRepository;

/**
 * Repräsentiert ein Repository für Spring Data Neo4j zum Zugriff auf die Datenbank für Objekte vom Typ Baujahr
 */
public interface BaujahrRepository extends CrudRepository<Baujahr, String> {

    /**
     * Liefert eine Entität vom Typ Baujahr anhand des Wertes
     *
     * @param value der Wert des zu findenen Baujahrknotens
     * @return das Baujahr, welches den gesuchten Wert besitzt.
     */
    public Baujahr findByValue(double value);
}
