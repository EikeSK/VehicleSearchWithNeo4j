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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static support.TestUtils.metaDataWith;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
public class SearchEngineImplIntegrationTest {

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

    @Test
    public void testFindNodeByBrandName() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"));

        final Collection<VehicleNode> searchResult = _searchEngine.search("Audi");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.iterator().next().getName(), equalTo("Audi A4 B8 Kombi"));
    }

    @Test
    public void testFindNodeWithTwoSearchTerms() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("VW Golf 7 Kombi"));

        final Collection<VehicleNode> searchResult = _searchEngine.search("VW Kombi");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.iterator().next().getName(), equalTo("VW Golf 7 Kombi"));
    }

    @Test
    public void testFindNodeWithExactSearchTerm() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("VW Golf 7 Kombi"));

        final Collection<VehicleNode> searchResult = _searchEngine.search("VW Golf 7 Kombi");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.iterator().next().getName(), equalTo("VW Golf 7 Kombi"));
    }

    @Test
    public void testFindNodeWithInvertedSearchTerm() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("VW Golf 7 Kombi"));

        final Collection<VehicleNode> searchResult = _searchEngine.search("7 Golf Kombi VW");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.iterator().next().getName(), equalTo("VW Golf 7 Kombi"));
    }

    @Test
    public void testShouldFindMultipleNodesForSearchTerm() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("VW Golf 7 Kombi"));
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"));

        final Collection<VehicleNode> searchResult = _searchEngine.search("Kombi");

        assertThat(searchResult, hasSize(2));
    }

    @Test
    public void testShouldFindNodeCaseInsensitive() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"));

        final Collection<VehicleNode> searchResult = _searchEngine.search("b8");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.iterator().next().getName(), equalTo("Audi A4 B8 Kombi"));
    }

    @Test
    public void testShouldFindNodeWithAdditionalTerms() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"), new HashSet<>(Arrays.asList(metaDataWith("benzin"),metaDataWith("2008"))));

        final Collection<VehicleNode> searchResult = _searchEngine.search("benzin");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.iterator().next().getName(), equalTo("Audi A4 B8 Kombi"));
    }

    @Test
    public void testShouldFindNothingIfSearchTermContainsUnknownToken() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"));

        final Collection<VehicleNode> searchResult = _searchEngine.search("BMW");

        assertThat(searchResult, hasSize(0));
    }

    @Test
    public void testShouldFindNodeWithDotInSearchTerm() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"), new HashSet<>(Arrays.asList(metaDataWith("2.4"))));

        final Collection<VehicleNode> searchResult = _searchEngine.search("A4 2.4");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.iterator().next().getName(), equalTo("Audi A4 B8 Kombi"));
    }

    @Test
    public void testShouldFindNodeByIncompleteToken() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"));

        final Collection<VehicleNode> searchResult = _searchEngine.search("Aud");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.iterator().next().getName(), equalTo("Audi A4 B8 Kombi"));
    }

    @Test
    public void testShouldFindNodeByMultipleIncompleteTokens() throws Exception {
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName("Audi A4 B8 Kombi"));

        final Collection<VehicleNode> searchResult = _searchEngine.search("Aud Ko");

        assertThat(searchResult, hasSize(1));
        assertThat(searchResult.iterator().next().getName(), equalTo("Audi A4 B8 Kombi"));

    }

    private static VehicleNode vehicleNodeWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}