package domain;

import org.springframework.data.neo4j.annotation.GraphId;

/**
 * Markiert erbende Klassen als Entität für Neo4j, indem es ihnen eine GraphId als Feld gibt. Diese GraphId wird von
 * Spring Data Neo4j automatisch gesetzt.
 */
public abstract class AbstractNeo4jEntity {
    @GraphId
    private Long _id;
}
