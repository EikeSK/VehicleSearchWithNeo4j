package repositories;

import domain.VehicleNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import config.TestContext;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
public class VehicleNodeRepositoryImplIntegrationTest {

    @Autowired
    private VehicleModelRepository _vehicleModelRepository;

    @Test
    @Transactional
    public void test() throws Exception {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName("Test entity");

        _vehicleModelRepository.save(vehicleNode);

        final String query = "MATCH (n) RETURN n";

        final Iterable<VehicleNode> modelsByQuery = _vehicleModelRepository.findModelsByQuery(query);
        final VehicleNode result = modelsByQuery.iterator().next();

        assertThat(result, notNullValue());
        assertThat(result.getName(), equalTo("Test entity"));

    }
}