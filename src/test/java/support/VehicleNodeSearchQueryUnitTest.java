package support;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VehicleNodeSearchQueryUnitTest {

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
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().withTerm("Testentity");
        final String expetedQuery = "MATCH (testentity: {name:'Testentity'})-[:MATCHES_FOR]->(modell) RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expetedQuery, cypherQuery);
    }

    @Test
    public void testCypherQueryWithStartAndOneOtherTerms() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().withStartTerm("Startterm").withTerm("Test1");
        final String expectedQuery = "START a=node:terms(name='Startterm') MATCH (a)-[:MATCHES_FOR]->(modell), " +
                "(test1: {name:'Test1'})-[:MATCHES_FOR]->(modell) " +
                "RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testCypherQueryWithStartAndMultipleOtherTerms() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().withStartTerm("Startterm").withTerm("Test1").withTerm("Test2");
        final String expectedQuery = "START a=node:terms(name='Startterm') MATCH (a)-[:MATCHES_FOR]->(modell), " +
                "(test1: {name:'Test1'})-[:MATCHES_FOR]->(modell), (test2: {name:'Test2'})-[:MATCHES_FOR]->(modell) " +
                "RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }
}