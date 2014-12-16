package service;

import domain.VehicleNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import config.ProductiveContext;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductiveContext.class})
public class VehicleDataHelperTest {

    @Autowired
    private VehicleDataPersistenceService _vehicleDataPersistenceService;

    @Test
    public void fillDatabaseWithVehicleData() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("Audi A4 B8 Kombi"), new HashSet<>(Arrays.asList("Avant")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("Audi A6 B6 Kombi"), new HashSet<>(Arrays.asList("Avant")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("BMW 1er E87 Coupe"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("BMW 1er F21 Coupe"), new HashSet<>(Arrays.asList("neuster","neuer","2012")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("VW Bora 1J Limousine"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("Audi A3 8P Cabrio"), new HashSet<>(Arrays.asList("2003", "2013")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("VW Golf 7 Kombi"), new HashSet<>(Arrays.asList("seit", "2012", "neuster", "neuer")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("VW Golf 6 Kombi"), new HashSet<>(Arrays.asList("2008", "2012", "diesel", "benzin")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("Skoda Octavia Scout Kombi"), new HashSet<>(Arrays.asList("2007", "2012", "diesel", "benzin")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModelWithName("Skoda Octavia E5 Limousine"), new HashSet<>(Arrays.asList("seit", "2013", "neuster", "neuer")));
    }

    private VehicleNode vehicleModelWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}