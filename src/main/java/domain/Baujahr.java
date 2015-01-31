package domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

/**
 * Repräsentiert ein Baujahr als Knoten mit einem vergleichbaren Wert in der Graphdatenbank Neo4j
 * Objekte dieser Klasse können über eine MATCHES_FOR-Verbindung mit Fahrzeugknoten (VehicleNode) verbunden werden.
 */
@NodeEntity
public class Baujahr extends AbstractNeo4jEntity {

    private int value;

    @RelatedTo(type = "MATCHES_FOR", direction = Direction.OUTGOING)
    @Fetch
    private Set<VehicleNode> relatedNodes;

    /**
     * Liefert den Wert des Baujahres
     *
     * @return der Wert des Baujahres
     */
    public double getValue() {
        return value;
    }

    /**
     * Setzt den Wert des Baujahres
     *
     * @param value der zu setzende Wert
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Fügt dem Baujahr einen Fahrzeugknoten hinzu, zudem das Baujahr über eine MATCHES_FOR-Verbindung verbunden wird.
     *
     * @param node Der Fahrzeugknoten, zudem das Baujahr verbunden werden soll.
     */
    public void addRelationTo(final VehicleNode node) {
        if (relatedNodes == null) {
            relatedNodes = new HashSet<>();
        }
        relatedNodes.add(node);
    }

    /**
     * Überprüft anhand des Wertes, ob zwei Objekte dieser Klasse gleich sind.
     *
     * @param o Ein Objekt der Klasse Baujahr, mit dem dieses verglichen werden soll
     * @return <code>true</code>, wenn beide Baujahre anhand ihres Wertes gleich sind, andernfalls <code>false</code>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Baujahr that = (Baujahr) o;

        return Double.compare(that.getValue(), getValue()) == 0;
    }

    /**
     * Liefert die verbundenen Fahrzeugknoten
     *
     * @return die verbundenen Fahrzeugknoten
     */
    public Set<VehicleNode> getRelatedNodes() {
        return relatedNodes;
    }
}
