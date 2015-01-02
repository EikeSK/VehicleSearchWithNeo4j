package search;

import config.TestContext;
import domain.VehicleNode;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import service.VehicleDataPersistenceServiceImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
public class SearchEngineImplAcceptanceTest {

    @Autowired
    private SearchEngine _searchEngine;

    @Autowired
    private VehicleDataPersistenceServiceImpl _vehicleDataPersistenceService;

    @Autowired
    private VehicleNodeRepository _vehicleNodeRepository;

    @Autowired
    private TermRepository _termRepository;

    @After
    public void tearDown() throws Exception {
        _vehicleNodeRepository.deleteAll();
        _termRepository.deleteAll();
    }

    //Einfache Suche

    @Test
    public void testFindNodeByBrand() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B7 Kombi"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E81 Schrägheck"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 3er E30 Coupe"));

        final Collection<String> searchResultForAudi = getVehicleNodeNamesFor(_searchEngine.search("Audi"));
        final Collection<String> searchResultForBMW = getVehicleNodeNamesFor(_searchEngine.search("BMW"));

        assertThat(searchResultForAudi, hasSize(1));
        assertThat(searchResultForAudi, contains("Audi A4 B7 Kombi"));

        assertThat(searchResultForBMW, hasSize(2));
        assertThat(searchResultForBMW, containsInAnyOrder("BMW 1er E81 Schrägheck", "BMW 3er E30 Coupe"));
    }

    @Test
    public void testFindNodeBySeries() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E81 Schrägheck"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B7 Kombi"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Coupe"));

        final Collection<String> searchResultFor1er = getVehicleNodeNamesFor(_searchEngine.search("1er"));
        final Collection<String> searchResultForA4 = getVehicleNodeNamesFor(_searchEngine.search("A4"));

        assertThat(searchResultFor1er, hasSize(1));
        assertThat(searchResultFor1er, contains("BMW 1er E81 Schrägheck"));

        assertThat(searchResultForA4, hasSize(2));
        assertThat(searchResultForA4, containsInAnyOrder("Audi A4 B7 Kombi", "Audi A4 B8 Coupe"));
    }

    @Test
    public void testFindByGeneration() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E81 Schrägheck"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Coupe"));

        final Collection<String> searchResultForE81 = getVehicleNodeNamesFor(_searchEngine.search("E81"));
        final Collection<String> searchResultForB8 = getVehicleNodeNamesFor(_searchEngine.search("B8"));

        assertThat(searchResultForE81, hasSize(1));
        assertThat(searchResultForE81, contains("BMW 1er E81 Schrägheck"));

        assertThat(searchResultForB8, hasSize(2));
        assertThat(searchResultForB8, containsInAnyOrder("Audi A4 B8 Kombi", "Audi A4 B8 Coupe"));
    }

    @Test
    public void testFindByModel() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E81 Kombi"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B7 Kombi"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Coupe"));

        final Collection<String> searchResultForKombi = getVehicleNodeNamesFor(_searchEngine.search("Kombi"));
        final Collection<String> searchResultForCoupe = getVehicleNodeNamesFor(_searchEngine.search("Coupe"));

        assertThat(searchResultForKombi, hasSize(2));
        assertThat(searchResultForKombi, containsInAnyOrder("BMW 1er E81 Kombi", "Audi A4 B7 Kombi"));

        assertThat(searchResultForCoupe, hasSize(1));
        assertThat(searchResultForCoupe, contains("Audi A4 B8 Coupe"));
    }

    @Test
    public void testFindByVariant() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"), new HashSet<>(Arrays.asList("Quattro")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B7 Coupe"), new HashSet<>(Arrays.asList("ultra")));

        final Collection<String> searchResultForQuattro = getVehicleNodeNamesFor(_searchEngine.search("Quattro"));

        assertThat(searchResultForQuattro, hasSize(1));
        assertThat(searchResultForQuattro, contains("Audi A4 B8 Kombi"));
    }


    // Einfache Merkmalsuche


    @Test
    public void testFindByConstructionYear() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E81 Schrägheck"), new HashSet<>(Arrays.asList("2007", "2011")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B6 Kombi"), new HashSet<>(Arrays.asList("2000", "2004")));

        final Collection<String> searchResultFor2007 = getVehicleNodeNamesFor(_searchEngine.search("2007"));
        final Collection<String> searchResultFor2011 = getVehicleNodeNamesFor(_searchEngine.search("2011"));
        final Collection<String> searchResultFor2004 = getVehicleNodeNamesFor(_searchEngine.search("2004"));

        assertThat(searchResultFor2007, hasSize(1));
        assertThat(searchResultFor2007, contains("BMW 1er E81 Schrägheck"));

        assertThat(searchResultFor2011, hasSize(1));
        assertThat(searchResultFor2011, contains("BMW 1er E81 Schrägheck"));

        assertThat(searchResultFor2004, hasSize(1));
        assertThat(searchResultFor2004, contains("Audi A4 B6 Kombi"));
    }

    @Test
    public void testFindByFuelInjectionType() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E81 Schrägheck"), new HashSet<>(Arrays.asList("TDI", "FSI")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B6 Kombi"), new HashSet<>(Arrays.asList("TDI")));

        final Collection<String> resultForTDI = getVehicleNodeNamesFor(_searchEngine.search("TDI"));
        final Collection<String> resultForFSI = getVehicleNodeNamesFor(_searchEngine.search("FSI"));

        assertThat(resultForTDI, hasSize(2));
        assertThat(resultForTDI, containsInAnyOrder("BMW 1er E81 Schrägheck", "Audi A4 B6 Kombi"));

        assertThat(resultForFSI, hasSize(1));
        assertThat(resultForFSI, contains("BMW 1er E81 Schrägheck"));
    }

    @Test
    public void testFindByGearboxType() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E81 Schrägheck"), new HashSet<>(Arrays.asList("automatik", "manuell")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B6 Kombi"), new HashSet<>(Arrays.asList("automatik")));

        final Collection<String> resultForAutomatik = getVehicleNodeNamesFor(_searchEngine.search("automatik"));
        final Collection<String> resultForManuell = getVehicleNodeNamesFor(_searchEngine.search("manuell"));

        assertThat(resultForAutomatik, hasSize(2));
        assertThat(resultForAutomatik, containsInAnyOrder("BMW 1er E81 Schrägheck", "Audi A4 B6 Kombi"));

        assertThat(resultForManuell, hasSize(1));
        assertThat(resultForManuell, contains("BMW 1er E81 Schrägheck"));
    }


    //Semantische Suche

    @Test
    public void testFindBySemantic() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E81 Schrägheck"), new HashSet<>(Arrays.asList("2007", "2011")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E88 Cabrio"), new HashSet<>(Arrays.asList("2008", "2013", "neuster", "neuer")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B6 Kombi"), new HashSet<>(Arrays.asList("2000", "2004")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"), new HashSet<>(Arrays.asList("2007", "neuster", "neuer")));

        final Collection<String> resultForNeusterBMW = getVehicleNodeNamesFor(_searchEngine.search("neuster BMW"));
        final Collection<String> resultForNeuerBMW = getVehicleNodeNamesFor(_searchEngine.search("neuer BMW"));
        final Collection<String> resultForNeuerBMW1er = getVehicleNodeNamesFor(_searchEngine.search("neuer BMW 1er"));
        final Collection<String> resultForNeusterAudiA4 = getVehicleNodeNamesFor(_searchEngine.search("neuster Audi A4"));

        assertThat(resultForNeusterBMW, hasSize(1));
        assertThat(resultForNeusterBMW, contains("BMW 1er E88 Cabrio"));

        assertThat(resultForNeuerBMW, hasSize(1));
        assertThat(resultForNeuerBMW, contains("BMW 1er E88 Cabrio"));

        assertThat(resultForNeuerBMW1er, hasSize(1));
        assertThat(resultForNeuerBMW1er, contains("BMW 1er E88 Cabrio"));

        assertThat(resultForNeusterAudiA4, hasSize(1));
        assertThat(resultForNeusterAudiA4, contains("Audi A4 B8 Kombi"));
    }


    // Kombinierte Suche

    @Test
    public void testCombinedSearch() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E81 Schrägheck"),
                new HashSet<>(Arrays.asList("2007", "2011", "automatik", "manuell", "2.0", "3.0", "diesel")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("BMW 1er E88 Cabrio"),
                new HashSet<>(Arrays.asList("2008", "2013", "automatik", "2.6", "benzin")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B6 Kombi"),
                new HashSet<>(Arrays.asList("2000", "2004", "manuell", "TDI", "2.4", "diesel", "Avant")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"),
                new HashSet<>(Arrays.asList("2007", "manuell", "TDI", "FSI", "2.6", "2.4", "benzin", "diesel", "neuer", "neuster", "Avant")));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Skoda Octavia E5 Kombi"),
                new HashSet<>(Arrays.asList("2013", "manuell", "FSI", "2.0", "benzin", "neuer", "neuster")));

        Collection<String> searchResult = getVehicleNodeNamesFor(_searchEngine.search("BMW 1er"));
        assertThat(searchResult, hasSize(2));
        assertThat(searchResult, containsInAnyOrder("BMW 1er E81 Schrägheck", "BMW 1er E88 Cabrio"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("1er BMW Cabrio"));
        assertThat(searchResult, hasSize(1));
        assertThat(searchResult, containsInAnyOrder("BMW 1er E88 Cabrio"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("Audi A4 2.4"));
        assertThat(searchResult, hasSize(2));
        assertThat(searchResult, containsInAnyOrder("Audi A4 B6 Kombi", "Audi A4 B8 Kombi"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("Audi A4 2.4 FSI"));
        assertThat(searchResult, hasSize(1));
        assertThat(searchResult, containsInAnyOrder("Audi A4 B8 Kombi"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("Audi A4 Avant diesel"));
        assertThat(searchResult, hasSize(2));
        assertThat(searchResult, containsInAnyOrder("Audi A4 B6 Kombi", "Audi A4 B8 Kombi"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("A4 automatik"));
        assertThat(searchResult, hasSize(0));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("1er automatik"));
        assertThat(searchResult, hasSize(2));
        assertThat(searchResult, containsInAnyOrder("BMW 1er E81 Schrägheck", "BMW 1er E88 Cabrio"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("kombi benzin"));
        assertThat(searchResult, hasSize(2));
        assertThat(searchResult, containsInAnyOrder("Audi A4 B8 Kombi", "Skoda Octavia E5 Kombi"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("neuer A4 diesel"));
        assertThat(searchResult, hasSize(1));
        assertThat(searchResult, containsInAnyOrder("Audi A4 B8 Kombi"));
    }

    private static Collection<String> getVehicleNodeNamesFor(final Collection<VehicleNode> vehicleNodes) {
        return vehicleNodes.parallelStream()
                .map(VehicleNode::getName)
                .collect(Collectors.toList());
    }

    private static VehicleNode vehicleNodeWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}