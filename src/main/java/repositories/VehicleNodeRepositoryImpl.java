package repositories;

import domain.VehicleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;

public class VehicleNodeRepositoryImpl implements VehicleNodeRepositoryCustom {

    @Autowired
    private Neo4jTemplate _neo4jTemplate;

    @Override
    public Iterable<VehicleNode> findModelsByQuery(String query) {
        return _neo4jTemplate.query(query, null).to(VehicleNode.class);
    }
}
