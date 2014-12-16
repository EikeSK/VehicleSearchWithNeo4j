package repositories;

import domain.VehicleNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import config.TestContext;
import service.VehicleDataPersistenceService;
import support.VehicleNodeSearchQuery;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
public class VehicleNodeRepositoryImplIntegrationTest {

    @Autowired
    private VehicleNodeRepository _vehicleNodeRepository;

    @Autowired
    private VehicleDataPersistenceService _vehicleDataPersistenceService;

    @Test
    @Transactional
    public void testFindNodesByQueryShouldReturnVehicleNode() throws Exception {
        final String nodeName = "BMW 3er E8 Cabrio";
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName(nodeName));

        final Iterable<VehicleNode> nodesByQuery = _vehicleNodeRepository.findNodesByQuery(VehicleNodeSearchQuery.query().withStartTerm("bmw").addTerm("e8"));

        assertNotNull(nodesByQuery);
        assertEquals(nodesByQuery.iterator().next().getName(), nodeName);
    }

    private VehicleNode vehicleModelWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}