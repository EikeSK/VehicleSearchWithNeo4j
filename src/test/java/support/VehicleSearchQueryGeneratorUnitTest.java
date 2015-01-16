package support;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.apache.commons.collections4.SetUtils.emptySet;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class VehicleSearchQueryGeneratorUnitTest {

    @Test
    public void testCreateCypherQueryWithOneTerm() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().addTerm("firstTerm");
        final String expectedQuery = "START _firstTerm=node:terms(\"name:*firstTerm*\") MATCH (_firstTerm)-[:MATCHES_FOR]->(node) RETURN node";

        final String cypherQuery = VehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testCreateCypherQueryWithMultipleTerms() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().addTerm("firstTerm").addTerm("secondTerm").addTerm("thirdTerm");
        final String expectedQuery = "START _firstTerm=node:terms(\"name:*firstTerm*\"), _secondTerm=node:terms(\"name:*secondTerm*\"), " +
                "_thirdTerm=node:terms(\"name:*thirdTerm*\") MATCH (_firstTerm)-[:MATCHES_FOR]->(node), " +
                "(_secondTerm)-[:MATCHES_FOR]->(node), (_thirdTerm)-[:MATCHES_FOR]->(node) RETURN node";

        final String cypherQuery = VehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testCreateCypherQueryWithDotInTerm() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().addTerm("2.4");
        final String expectedQuery = "START _2dot4=node:terms(\"name:*2.4*\") MATCH (_2dot4)-[:MATCHES_FOR]->(node) RETURN node";

        final String cypherQuery = VehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testShouldReturnCypherQueryIfSetIsEmpty() throws Exception {
        final VehicleNodeSearchQuery vehicleNodeSearchQuery = VehicleSearchQueryGenerator.generateSearchQueryFrom(emptySet());

        assertNotNull(vehicleNodeSearchQuery);
    }

    @Test
    public void testShouldReturnSearchQueryByOneToken() throws Exception {
        final VehicleNodeSearchQuery resultQuery = VehicleSearchQueryGenerator.generateSearchQueryFrom(new HashSet<>(Arrays.asList("firstTerm")));

        assertThat(resultQuery.getTerms(), hasSize(1));
        assertThat(resultQuery.getTerms().iterator().next(), equalTo("firstTerm"));
    }

    @Test
    public void testShouldReturnSearchQueryByMultipleTokens() throws Exception {
        final VehicleNodeSearchQuery resultQuery = VehicleSearchQueryGenerator.generateSearchQueryFrom(new HashSet<>(Arrays.asList("firstTerm", "secondTerm")));

        final List<String> termsFromResultQuery = new ArrayList<>(resultQuery.getTerms());

        assertThat(resultQuery.getTerms(), hasSize(2));
        assertThat(termsFromResultQuery, containsInAnyOrder("firstTerm", "secondTerm"));

    }

    @Test
    public void testShouldReturnCypherQueryForAutocompletion() throws Exception {
        final String cypherQuery = VehicleSearchQueryGenerator.generateCypherQueryForAutocompletion("Autocompletion");
        final String expectedQuery = "START n=node:terms(\"name:*Autocompletion*\") MATCH (n:Term) RETURN n";
        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testShouldReturnCypherQueryWithComparisonOperation() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query()
                .addTerm("Test")
                .addComparisonOperations(new ComparisonOperation("baujahr", Operator.GREATER, 2006));
        final String cypherQuery = VehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        final String expectedQuery = "START _Test=node:terms(\"name:*Test*\") MATCH (_Test)-[:MATCHES_FOR]->(node), (_range_baujahr)-[:MATCHES_FOR]->(node) " +
                "WHERE _range_baujahr.value > 2006 RETURN node";

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testShouldReturnCypherQueryWithMultipleComparisonOperations() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query()
                .addTerm("Test")
                .addComparisonOperations(new ComparisonOperation("ps", Operator.SMALLER, 220.5))
                .addComparisonOperations(new ComparisonOperation("baujahr", Operator.GREATER, 2006));
        final String cypherQuery = VehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        final String expectedQuery = "START _Test=node:terms(\"name:*Test*\") MATCH (_Test)-[:MATCHES_FOR]->(node), " +
                "(_range_baujahr)-[:MATCHES_FOR]->(node), (_range_ps)-[:MATCHES_FOR]->(node) " +
                "WHERE _range_baujahr.value > 2006 AND _range_ps.value < 220.5 " +
                "RETURN node";

        assertEquals(expectedQuery, cypherQuery);
    }

    /*    @Test
    public void testShouldReturnCypherQueryWithComparisonOperation() throws Exception {
        final NodeMetaData nodeMetaData = new NodeMetaData();
        nodeMetaData.setName("2008");
        nodeMetaData.setUnit("Baujahr");
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().addTerm("firstTerm").addComparisionOperation(new ComparisonOperation(Operator.GREATER, nodeMetaData));
        final String expectedQuery = "START _firstTerm=node:terms(\"name:*firstTerm*\") " +
                "MATCH (_firstTerm)-[:MATCHES_FOR]->(modell), (_2008)-[:MATCHES_FOR]->(node)" +
                "WHERE toFloat(_2008.name) > 2008 AND _2008.unit = \"baujahr\" " +
                "RETURN DISTINCT(modell)";

        final String cypherQuery = VehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }*/

}