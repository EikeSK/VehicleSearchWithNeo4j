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
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static support.TestUtils.*;

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
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B7 Kombi"));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("BMW 1er E81 Schrägheck"));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("BMW 3er E30 Coupe"));

        final Collection<String> searchResultForAudi = getVehicleNodeNamesFor(_searchEngine.search("Audi"));
        final Collection<String> searchResultForBMW = getVehicleNodeNamesFor(_searchEngine.search("BMW"));

        assertThat(searchResultForAudi, hasSize(1));
        assertThat(searchResultForAudi, contains("Audi A4 B7 Kombi"));

        assertThat(searchResultForBMW, hasSize(2));
        assertThat(searchResultForBMW, containsInAnyOrder("BMW 1er E81 Schrägheck", "BMW 3er E30 Coupe"));
    }

    @Test
    public void testFindNodeBySeries() throws Exception {
        _vehicleDataPersistenceService.save(vehicleNodeWithName("BMW 1er E81 Schrägheck"));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B7 Kombi"));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B8 Coupe"));

        final Collection<String> searchResultFor1er = getVehicleNodeNamesFor(_searchEngine.search("1er"));
        final Collection<String> searchResultForA4 = getVehicleNodeNamesFor(_searchEngine.search("A4"));

        assertThat(searchResultFor1er, hasSize(1));
        assertThat(searchResultFor1er, contains("BMW 1er E81 Schrägheck"));

        assertThat(searchResultForA4, hasSize(2));
        assertThat(searchResultForA4, containsInAnyOrder("Audi A4 B7 Kombi", "Audi A4 B8 Coupe"));
    }

    @Test
    public void testFindByGeneration() throws Exception {
        _vehicleDataPersistenceService.save(vehicleNodeWithName("BMW 1er E81 Schrägheck"));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B8 Kombi"));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B8 Coupe"));

        final Collection<String> searchResultForE81 = getVehicleNodeNamesFor(_searchEngine.search("E81"));
        final Collection<String> searchResultForB8 = getVehicleNodeNamesFor(_searchEngine.search("B8"));

        assertThat(searchResultForE81, hasSize(1));
        assertThat(searchResultForE81, contains("BMW 1er E81 Schrägheck"));

        assertThat(searchResultForB8, hasSize(2));
        assertThat(searchResultForB8, containsInAnyOrder("Audi A4 B8 Kombi", "Audi A4 B8 Coupe"));
    }

    @Test
    public void testFindByModel() throws Exception {
        _vehicleDataPersistenceService.save(vehicleNodeWithName("BMW 1er E81 Kombi"));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B7 Kombi"));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B8 Coupe"));

        final Collection<String> searchResultForKombi = getVehicleNodeNamesFor(_searchEngine.search("Kombi"));
        final Collection<String> searchResultForCoupe = getVehicleNodeNamesFor(_searchEngine.search("Coupe"));

        assertThat(searchResultForKombi, hasSize(2));
        assertThat(searchResultForKombi, containsInAnyOrder("BMW 1er E81 Kombi", "Audi A4 B7 Kombi"));

        assertThat(searchResultForCoupe, hasSize(1));
        assertThat(searchResultForCoupe, contains("Audi A4 B8 Coupe"));
    }

    @Test
    public void testFindByVariant() throws Exception {
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B8 Kombi"), vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("Quattro"))));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B7 Coupe"), vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("ultra"))));

        final Collection<String> searchResultForQuattro = getVehicleNodeNamesFor(_searchEngine.search("Quattro"));

        assertThat(searchResultForQuattro, hasSize(1));
        assertThat(searchResultForQuattro, contains("Audi A4 B8 Kombi"));
    }


    // Einfache Merkmalsuche


    @Test
    public void testFindByConstructionYear() throws Exception {
        _vehicleDataPersistenceService.save(vehicleNodeWithName("BMW 1er E81 Schrägheck"), vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("2007", "2011"))));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B6 Kombi"), vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("2000", "2004"))));

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
    public void testFindByConstructionYearRange() throws Exception {
        _vehicleDataPersistenceService.save(vehicleNodeWithName("BMW 1er E81 Schrägheck"), vehicleMetaDataWithTermsAndBaujahr(Collections.<String>emptySet(), 2007, 2011));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B6 Kombi"), vehicleMetaDataWithTermsAndBaujahr(Collections.<String>emptySet(), 2000, 2004));

        final Collection<String> searchResultForGreater2007 = getVehicleNodeNamesFor(_searchEngine.search("BMW; Baujahr > 2007"));
        final Collection<String> searchResultSmaller2011 = getVehicleNodeNamesFor(_searchEngine.search("BMW; Baujahr < 2012"));
        final Collection<String> searchResultForRange = getVehicleNodeNamesFor(_searchEngine.search("Audi; Baujahr > 2001; Baujahr < 2003"));

        assertThat(searchResultForGreater2007, hasSize(1));
        assertThat(searchResultForGreater2007, contains("BMW 1er E81 Schrägheck"));

        assertThat(searchResultSmaller2011, hasSize(1));
        assertThat(searchResultSmaller2011, contains("BMW 1er E81 Schrägheck"));

        assertThat(searchResultForRange, hasSize(1));
        assertThat(searchResultForRange, contains("Audi A4 B6 Kombi"));
    }


    // Kombinierte Suche

    @Test
    public void testCombinedSearch() throws Exception {
        _vehicleDataPersistenceService.save(vehicleNodeWithName("BMW 1er E81 Schrägheck"), vehicleMetaDataWithTermsAndBaujahr(Collections.<String>emptySet(), 2007, 2011));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("BMW 1er E88 Cabrio"), vehicleMetaDataWithTermsAndBaujahr(Collections.<String>emptySet(), 2008, 2013));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B6 Kombi"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("Avant")), 2000, 2004));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Audi A4 B8 Kombi"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("Avant")), 2007));
        _vehicleDataPersistenceService.save(vehicleNodeWithName("Skoda Octavia E5 Kombi"), vehicleMetaDataWithTermsAndBaujahr(Collections.<String>emptySet(), 2013));

        Collection<String> searchResult = getVehicleNodeNamesFor(_searchEngine.search("BMW 1er"));
        assertThat(searchResult, hasSize(2));
        assertThat(searchResult, containsInAnyOrder("BMW 1er E81 Schrägheck", "BMW 1er E88 Cabrio"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("1er BMW Cabrio"));
        assertThat(searchResult, hasSize(1));
        assertThat(searchResult, containsInAnyOrder("BMW 1er E88 Cabrio"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("Audi A4 Avant"));
        assertThat(searchResult, hasSize(2));
        assertThat(searchResult, containsInAnyOrder("Audi A4 B6 Kombi", "Audi A4 B8 Kombi"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search(" Audi A4; Baujahr > 2001"));
        assertThat(searchResult, hasSize(2));
        assertThat(searchResult, containsInAnyOrder("Audi A4 B6 Kombi", "Audi A4 B8 Kombi"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("Kombi; Baujahr < 2014"));
        assertThat(searchResult, hasSize(3));
        assertThat(searchResult, containsInAnyOrder("Audi A4 B6 Kombi", "Audi A4 B8 Kombi", "Skoda Octavia E5 Kombi"));

        searchResult = getVehicleNodeNamesFor(_searchEngine.search("BMW 1er; Baujahr = 2010"));
        assertThat(searchResult, hasSize(2));
        assertThat(searchResult, containsInAnyOrder("BMW 1er E81 Schrägheck", "BMW 1er E88 Cabrio"));
    }

    private static Collection<String> getVehicleNodeNamesFor(final Collection<VehicleNode> vehicleNodes) {
        return vehicleNodes.parallelStream()
                .map(VehicleNode::getName)
                .collect(Collectors.toList());
    }
}