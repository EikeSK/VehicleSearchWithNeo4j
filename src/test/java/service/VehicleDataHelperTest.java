package service;

import domain.VehicleNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import config.ProductiveContext;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductiveContext.class})
public class VehicleDataHelperTest {

    @Autowired
    private VehicleDataPersistenceServiceImpl _vehicleDataPersistenceService;

    @Test
    public void fillDatabaseWithVehicleData() throws Exception {
        Map<VehicleNode, Set<String>> batch = new HashMap<>();
        batch.put(vehicleModelWithName("Audi A4 B8 Kombi"), new HashSet<>(Arrays.asList("Avant")));
        batch.put(vehicleModelWithName("Audi A6 B6 Kombi"), new HashSet<>(Arrays.asList("Avant")));
        batch.put(vehicleModelWithName("BMW 1er E87 Coupe"), new HashSet<>());
        batch.put(vehicleModelWithName("BMW 1er F21 Coupe"), new HashSet<>(Arrays.asList("neuster", "neuer", "2012")));
        batch.put(vehicleModelWithName("VW Bora 1J Limousine"), new HashSet<>());
        batch.put(vehicleModelWithName("Audi A3 8P Cabrio"), new HashSet<>(Arrays.asList("2003", "2013")));
        batch.put(vehicleModelWithName("VW Golf 7 Kombi"), new HashSet<>(Arrays.asList("seit", "2012", "neuster", "neuer")));
        batch.put(vehicleModelWithName("VW Golf 6 Kombi"), new HashSet<>(Arrays.asList("2008", "2012", "diesel", "benzin")));
        batch.put(vehicleModelWithName("Skoda Octavia Scout Kombi"), new HashSet<>(Arrays.asList("2007", "2012", "diesel", "benzin")));
        batch.put(vehicleModelWithName("Skoda Octavia E5 Limousine"), new HashSet<>(Arrays.asList("seit", "2013", "neuster", "neuer")));
        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batch);
    }

    private VehicleNode vehicleModelWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}