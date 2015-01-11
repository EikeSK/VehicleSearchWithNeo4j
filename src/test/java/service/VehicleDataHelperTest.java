package service;

import config.ProductiveContext;
import domain.NodeMetaData;
import domain.VehicleNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static support.TestUtils.metaDataWith;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductiveContext.class})
public class VehicleDataHelperTest {

    @Autowired
    private VehicleDataPersistenceServiceImpl _vehicleDataPersistenceService;

    @Test
    public void fillDatabaseWithVehicleData() throws Exception {
        Map<VehicleNode, Set<NodeMetaData>> batch = new HashMap<>();
        batch.put(vehicleModelWithName("Audi A4 B8 Kombi"), new HashSet<>(Arrays.asList(metaDataWith("Avant"))));
        batch.put(vehicleModelWithName("Audi A6 B6 Kombi"), new HashSet<>(Arrays.asList(metaDataWith("Avant"))));
        batch.put(vehicleModelWithName("BMW 1er E87 Coupe"), Collections.<NodeMetaData>emptySet());
        batch.put(vehicleModelWithName("BMW 1er F21 Coupe"), new HashSet<>(Arrays.asList(metaDataWith("neuster"), metaDataWith("neuer"), metaDataWith("2012", "Baujahr"))));
        batch.put(vehicleModelWithName("VW Bora 1J Limousine"), Collections.<NodeMetaData>emptySet());
        batch.put(vehicleModelWithName("Audi A3 8P Cabrio"), new HashSet<>(Arrays.asList(metaDataWith("2003", "Baujahr"), metaDataWith("2013", "Baujahr"))));
        batch.put(vehicleModelWithName("VW Golf 7 Kombi"), new HashSet<>(Arrays.asList(metaDataWith("seit"), metaDataWith("2012", "Baujahr"), metaDataWith("neuster"), metaDataWith("neuer"))));
        batch.put(vehicleModelWithName("VW Golf 6 Kombi"), new HashSet<>(Arrays.asList(metaDataWith("2008"), metaDataWith("2012"), metaDataWith("diesel"), metaDataWith("benzin"))));
        batch.put(vehicleModelWithName("Skoda Octavia Scout Kombi"), new HashSet<>(Arrays.asList(metaDataWith("2007", "Baujahr"), metaDataWith("2012", "Baujahr"), metaDataWith("diesel"), metaDataWith("benzin"))));
        batch.put(vehicleModelWithName("Skoda Octavia E5 Limousine"), new HashSet<>(Arrays.asList(metaDataWith("seit"), metaDataWith("2013", "Baujahr"), metaDataWith("neuster"), metaDataWith("neuer"))));
        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batch);
    }

    private VehicleNode vehicleModelWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}