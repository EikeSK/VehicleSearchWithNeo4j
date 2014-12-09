package support;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class VehicleModelSearchQueryUnitTest {

    @Test
    public void testCypherQueryOnlyWithStartTerm() throws Exception {
        final String cypherQuery =  VehicleModelSearchQuery.query().withStartTerm("Testentity").toCypherQuery();

        assertThat(cypherQuery, equalTo("START a=node:terms(name:'Testentity') MATCH (a)-[:MATCHES_FOR]->(modell) RETURN modell"));
    }

    @Test
    public void testCypherQueryOnlyWithTerms() throws Exception {
        final String cypherQuery =  VehicleModelSearchQuery.query().withTerm("Testentity").toCypherQuery();

        assertThat(cypherQuery, equalTo("MATCH (testentity {name:'Testentity'})-[:MATCHES_FOR]->(modell) RETURN modell"));
    }

    @Test
    public void testCypherQueryWithStartAndMultipleOtherTerms() throws Exception {
        final String cypherQuery = VehicleModelSearchQuery.query().withStartTerm("Startterm").withTerm("Test1").withTerm("Test2").toCypherQuery();

        final String expectedQuery = "START a=node:terms(name:'Startterm') MATCH (a)-[:MATCHES_FOR]->(modell), " +
                "(test1: {name:'Test1'})-[:MATCHES_FOR]->(modell), (test2: {name:'Test2'})-[:MATCHES_FOR]->(modell) " +
                "RETURN modell";
        assertThat(cypherQuery, equalTo(expectedQuery));
    }
}