package support;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.apache.commons.collections4.SetUtils.emptySet;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class VehicleSearchQueryGeneratorUnitTest {

    private VehicleSearchQueryGenerator _vehicleSearchQueryGenerator;

    @Before
    public void setUp() throws Exception {
        _vehicleSearchQueryGenerator = new VehicleSearchQueryGenerator();
    }

    @Test
    public void testCypherQueryOnlyWithStartTerm() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().withStartTerm("Testentity");
        final String expetedQuery = "START a=node:terms(name='Testentity') MATCH (a)-[:MATCHES_FOR]->(modell) RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expetedQuery, cypherQuery);
    }

    @Test
    public void testCypherQueryOnlyWithTerms() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().addTerm("Testentity");
        final String expetedQuery = "MATCH (_testentity:Term{name:'Testentity'})-[:MATCHES_FOR]->(modell) RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expetedQuery, cypherQuery);
    }

    @Test
    public void testCypherQueryWithStartAndOneOtherTerms() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().withStartTerm("Startterm").addTerm("Test1");
        final String expectedQuery = "START a=node:terms(name='Startterm') MATCH (a)-[:MATCHES_FOR]->(modell), " +
                "(_test1:Term{name:'Test1'})-[:MATCHES_FOR]->(modell) " +
                "RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testCypherQueryWithStartAndMultipleOtherTerms() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().withStartTerm("Startterm").addTerm("Test1").addTerm("Test2");
        final String expectedQuery = "START a=node:terms(name='Startterm') MATCH (a)-[:MATCHES_FOR]->(modell), " +
                "(_test1:Term{name:'Test1'})-[:MATCHES_FOR]->(modell), (_test2:Term{name:'Test2'})-[:MATCHES_FOR]->(modell) " +
                "RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testCreateSearchQueryWithDotInAdditionalTerm() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().withStartTerm("Startterm").addTerm("2.4");
        final String expectedQuery = "START a=node:terms(name='Startterm') MATCH (a)-[:MATCHES_FOR]->(modell), " +
                "(_2dot4:Term{name:'2.4'})-[:MATCHES_FOR]->(modell) RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testCreateSearchQueryWithOneTermShouldContainStartTerm() throws Exception {
        final String startTerm = "StartTerm";
        final VehicleNodeSearchQuery searchQuery = _vehicleSearchQueryGenerator.generateSearchQueryFrom(new HashSet<>(Arrays.asList(startTerm)));

        assertEquals(startTerm, searchQuery.getStartTerm());
    }

    @Test
    public void testSearchQueryWithMultipleTermsShouldContainStartTermAndAdditionalTerms() throws Exception {
        final String first = "firstTerm";
        final String second = "secondTerm";

        final VehicleNodeSearchQuery searchQuery = _vehicleSearchQueryGenerator.generateSearchQueryFrom(new HashSet<>(Arrays.asList(first, second)));

        assertNotNull(searchQuery.getStartTerm());
        assertThat(searchQuery.getTerms(), hasSize(1));
        assertNotNull(searchQuery.getTerms().iterator().next());
    }

    @Test
    public void testShouldReturnSearchQueryIfSetIsEmpty() throws Exception {
        final VehicleNodeSearchQuery vehicleNodeSearchQuery = _vehicleSearchQueryGenerator.generateSearchQueryFrom(emptySet());

        assertNotNull(vehicleNodeSearchQuery);
    }
}