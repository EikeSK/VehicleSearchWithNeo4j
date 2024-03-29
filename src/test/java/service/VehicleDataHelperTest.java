package service;

import config.ProductiveContext;
import domain.VehicleNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import support.VehicleMetaData;

import java.util.*;

import static support.TestUtils.vehicleMetaDataWithTerms;
import static support.TestUtils.vehicleMetaDataWithTermsAndBaujahr;

/**
 * Test zum Befüllen der produktiven Datenbank mit einem Test-Datensatz zu Demonstrationszwecken.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductiveContext.class})
public class VehicleDataHelperTest {

    @Autowired
    private VehicleDataPersistenceServiceImpl _vehicleDataPersistenceService;

    @Test
    public void fillDatabaseWithVehicleData() throws Exception {
        Map<VehicleNode, VehicleMetaData> batch = new HashMap<>();
        batch.put(vehicleNodeWithName("Audi A4 B8 Kombi"), vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("Avant"))));
        batch.put(vehicleNodeWithName("Audi A6 B6 Kombi"), vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("Avant"))));
        batch.put(vehicleNodeWithName("Audi A3 8P Cabrio"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("2003", "2013")), 2003, 2013));
        batch.put(vehicleNodeWithName("BMW 1er E87 Coupe"), vehicleMetaDataWithTerms(Collections.<String>emptySet()));
        batch.put(vehicleNodeWithName("BMW 1er F21 Coupe"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("neuster", "neuer", "2012")), 2012, 2014));
        batch.put(vehicleNodeWithName("VW Bora 1J Limousine"), vehicleMetaDataWithTerms(Collections.<String>emptySet()));
        batch.put(vehicleNodeWithName("VW Golf 7 Kombi"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("seit", "2012", "neuster", "neuer")), 2012, 2014));
        batch.put(vehicleNodeWithName("VW Golf 6 Kombi"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("2008", "2012", "diesel", "benzin")), 2008, 2012));
        batch.put(vehicleNodeWithName("Skoda Octavia Scout Kombi"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("2007", "2012", "diesel", "benzin")), 2007, 2012));
        batch.put(vehicleNodeWithName("Skoda Octavia E5 Limousine"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("seit", "2013", "neuster", "neuer")), 2013, 2014));
        _vehicleDataPersistenceService.saveBatch(batch);
    }

    private VehicleNode vehicleNodeWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}