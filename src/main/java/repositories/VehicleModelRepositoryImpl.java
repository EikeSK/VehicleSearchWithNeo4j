package repositories;

import domain.VehicleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;

public class VehicleModelRepositoryImpl implements VehicleModelRepositoryCustom {

    @Autowired
    private Neo4jTemplate _neo4jTemplate;

    @Override
    public Iterable<VehicleModel> findModelsByQuery(String query) {
        return _neo4jTemplate.query(query, null).to(VehicleModel.class);
    }
}
