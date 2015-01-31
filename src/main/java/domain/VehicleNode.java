package domain;

import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Repräsentiert einen Fahrzeugknoten aus der Graphdatenbank Neo4j
 */
@NodeEntity
public class VehicleNode extends AbstractNeo4jEntity {

    private String name;

    /**
     * Liefert den Namen des Fahrzeugknotens
     *
     * @return der Namme des Fahrzeugknotens
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Fahrzeugknotens
     * @param name der Name des Fahhrzeugknotens
     */
    public void setName(String name) {
        this.name = name;
    }
}
