package repositories;

import config.TestContext;
import domain.Term;
import domain.VehicleNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import service.VehicleDataPersistenceService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
public class TermRepositoryImplIntegrationTest {

    @Autowired
    private TermRepository _termRepository;

    @Autowired
    private VehicleDataPersistenceService _vehicleDataPersistenceService;

    @Test
    @Transactional
    public void testFindNodesByQueryShouldReturnVehicleNode() throws Exception {
        final String nodeName = "Volkswagen Amarok";
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName(nodeName));

        final Iterable<Term> termsByQuery = _termRepository.findByIncompleteName("Volk");

        assertNotNull(termsByQuery);
        assertEquals(termsByQuery.iterator().next().getName(), "volkswagen");
    }

    private VehicleNode vehicleModelWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}