package repositories;

import domain.VehicleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import support.VehicleNodeSearchQuery;
import support.VehicleSearchQueryGenerator;

@Service
public class VehicleNodeRepositoryImpl implements VehicleNodeRepositoryCustom {

    @Autowired
    private Neo4jTemplate _neo4jTemplate;

    @Autowired
    private VehicleSearchQueryGenerator _vehicleSearchQueryGenerator;

    @Override
    public Iterable<VehicleNode> findNodesByQuery(VehicleNodeSearchQuery searchQuery) {
        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(searchQuery);
        return _neo4jTemplate.query(cypherQuery, null).to(VehicleNode.class);
    }
}
