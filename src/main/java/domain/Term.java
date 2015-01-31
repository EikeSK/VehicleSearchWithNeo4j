package domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.HashSet;
import java.util.Set;

/**
 * Repräsentiert einen Suchbegriff aus der Graphdatenbank Neo4j
 * Dabei wird der Name des Suchbegriffs in der Datenbank indexiert mit dem Indexnamen 'terms'.
 */
@NodeEntity
public class Term extends AbstractNeo4jEntity {

    @Indexed(indexName = "terms", indexType = IndexType.FULLTEXT, unique = true)
    private String name;

    @RelatedTo(type = "MATCHES_FOR", direction = Direction.OUTGOING)
    @Fetch
    private Set<VehicleNode> relatedNodes;

    /**
     * Liefert den Namen des Suchbegriffs
     *
     * @return Der Name des Suchbegriffs
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Suchbegriffs. Dieser wird indexiert und lässt sich über den Indexnamen 'terms' abrufenn
     * @param name Der Name des Suchbegriffs
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Fügt einen Fahrzeugknoten hinzu, zu dem der Suchbegriff über eine MATCHES_FOR-Verbindung verbunden ist.
     * @param node Der Fahrzeugknoten, zudem der Suchbegriff verbunden werden soll.
     */
    public void addRelationTo(final VehicleNode node) {
        if (relatedNodes == null) {
            relatedNodes = new HashSet<>();
        }
        relatedNodes.add(node);
    }

    /**
     * Überprüft anhand des Namens, ob zwei Objekte der Klasse Term gleich sind.
     *
     * @param o Der Suchbegriff, mit dem das Objekt dieser Klasse verglichen werden soll
     * @return <code>true</code>, wenn beide Suchbegriffe den gleichen Namen besitzen, andernfalls <code>false</code>
     */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        Term term = (Term) o;
        return name.equals(term.getName());
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public Set<VehicleNode> getRelatedNodes() {
        return relatedNodes;
    }
}
