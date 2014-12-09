package service;

import domain.VehicleModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import config.ProductiveContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductiveContext.class})
public class VehicleDataHelperTest {

    @Autowired
    private VehicleDataPersistenceService _vehicleDataPersistenceService;

    @Test
    public void fillDatabaseWithVehicleData() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("Audi A4 B8 Kombi"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("Audi A6 B6 Kombi"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("BMW 1er E87 Coupe"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("BMW 1er F21 Coupe"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("VW Bora 1J Limousine"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("VW Golf 7 Kombi"));
    }

    private VehicleModel vehicleModelWithName(final String name) {
        final VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setName(name);
        return vehicleModel;
    }
}