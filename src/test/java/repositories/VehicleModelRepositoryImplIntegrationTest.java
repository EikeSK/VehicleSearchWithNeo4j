package repositories;

import domain.VehicleModel;
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
public class VehicleModelRepositoryImplIntegrationTest {

    @Autowired
    private VehicleModelRepository _vehicleModelRepository;

    @Test
    @Transactional
    public void test() throws Exception {
        final VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setName("Test entity");

        _vehicleModelRepository.save(vehicleModel);

        final String query = "MATCH (n) RETURN n";

        final Iterable<VehicleModel> modelsByQuery = _vehicleModelRepository.findModelsByQuery(query);
        final VehicleModel result = modelsByQuery.iterator().next();

        assertThat(result, notNullValue());
        assertThat(result.getName(), equalTo("Test entity"));

    }
}