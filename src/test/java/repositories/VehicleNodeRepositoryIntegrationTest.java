package repositories;

import domain.VehicleNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import config.TestContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
public class VehicleNodeRepositoryIntegrationTest {

    @Autowired
    private VehicleModelRepository _vehicleModelRepository;

    @Autowired
    @Qualifier("graphDatabaseService")
    private GraphDatabaseService _graphDatabaseService;

    @Before
    public void setUp() {
        _vehicleModelRepository.deleteAll();
    }

    @Test
    public void testCRUD() throws Exception {
        final String modelName = "Audi A4 Kombi";
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(modelName);

        List<VehicleNode> vehicleNodes = new ArrayList<>();

        try (Transaction tx = _graphDatabaseService.beginTx()) {
            _vehicleModelRepository.save(vehicleNode);
            tx.success();
        }

        try (Transaction tx = _graphDatabaseService.beginTx()) {
            vehicleNodes = IteratorUtil.asList(_vehicleModelRepository.findAll());
            tx.success();
        }

        assertThat(vehicleNodes, hasSize(1));
        assertThat(vehicleNodes.get(0).getName(), equalTo(modelName));
    }

    @Test
    @Transactional
    public void testFindByName() throws Exception {
        final String modelName = "Audi A4 Kombi";
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(modelName);

        try (Transaction tx = _graphDatabaseService.beginTx()) {
            _vehicleModelRepository.save(vehicleNode);
            tx.success();
        }

        final List<VehicleNode> result = IteratorUtil.asList(_vehicleModelRepository.findByName(modelName));

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getName(), equalTo(modelName));

    }
}