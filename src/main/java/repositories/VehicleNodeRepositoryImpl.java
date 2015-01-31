package repositories;

import domain.VehicleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import support.VehicleNodeSearchQuery;
import support.VehicleSearchQueryGenerator;

/**
 * Repräsentiert eine Erweiterung des Repositoies zum Zugriff auf die Datenbank für Objekte vom Typ VehicleNode.
 */
@Service
public class VehicleNodeRepositoryImpl implements VehicleNodeRepositoryCustom {

    @Autowired
    private Neo4jTemplate _neo4jTemplate;

    @Override
    public Iterable<VehicleNode> findNodesByQuery(VehicleNodeSearchQuery searchQuery) {
        final String cypherQuery = VehicleSearchQueryGenerator.generateCypherQueryFrom(searchQuery);
        return _neo4jTemplate.query(cypherQuery, null).to(VehicleNode.class);
    }
}
