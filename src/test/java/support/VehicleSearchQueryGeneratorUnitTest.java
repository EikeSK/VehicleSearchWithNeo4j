package support;

import org.junit.Before;
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

    private VehicleSearchQueryGenerator _vehicleSearchQueryGenerator;

    @Before
    public void setUp() throws Exception {
        _vehicleSearchQueryGenerator = new VehicleSearchQueryGenerator();
    }

    @Test
    public void testCreateCypherQueryWithOneTerm() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().addTerm("firstTerm");
        final String expectedQuery = "START _firstTerm=node:terms(\"name:*firstTerm*\") MATCH (_firstTerm)-[:MATCHES_FOR]->(modell) RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testCreateCypherQueryWithMultipleTerms() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().addTerm("firstTerm").addTerm("secondTerm").addTerm("thirdTerm");
        final String expectedQuery = "START _firstTerm=node:terms(\"name:*firstTerm*\"), _secondTerm=node:terms(\"name:*secondTerm*\"), " +
                "_thirdTerm=node:terms(\"name:*thirdTerm*\") MATCH (_firstTerm)-[:MATCHES_FOR]->(modell), " +
                "(_secondTerm)-[:MATCHES_FOR]->(modell), (_thirdTerm)-[:MATCHES_FOR]->(modell) RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testCreateCypherQueryWithDotInTerm() throws Exception {
        final VehicleNodeSearchQuery query = VehicleNodeSearchQuery.query().addTerm("2.4");
        final String expectedQuery = "START _2dot4=node:terms(\"name:*2.4*\") MATCH (_2dot4)-[:MATCHES_FOR]->(modell) RETURN modell";

        final String cypherQuery = _vehicleSearchQueryGenerator.generateCypherQueryFrom(query);

        assertEquals(expectedQuery, cypherQuery);
    }

    @Test
    public void testShouldReturnCypherQueryIfSetIsEmpty() throws Exception {
        final VehicleNodeSearchQuery vehicleNodeSearchQuery = _vehicleSearchQueryGenerator.generateSearchQueryFrom(emptySet());

        assertNotNull(vehicleNodeSearchQuery);
    }

    @Test
    public void testShouldReturnSearchQueryByOneToken() throws Exception {
        final VehicleNodeSearchQuery resultQuery = _vehicleSearchQueryGenerator.generateSearchQueryFrom(new HashSet<>(Arrays.asList("firstTerm")));

        assertThat(resultQuery.getTerms(), hasSize(1));
        assertThat(resultQuery.getTerms().iterator().next(), equalTo("firstTerm"));
    }

    @Test
    public void testShouldReturnSearchQueryByMultipleTokens() throws Exception {
        final VehicleNodeSearchQuery resultQuery = _vehicleSearchQueryGenerator.generateSearchQueryFrom(new HashSet<>(Arrays.asList("firstTerm", "secondTerm")));

        final List<String> termsFromResultQuery = new ArrayList<>(resultQuery.getTerms());

        assertThat(resultQuery.getTerms(), hasSize(2));
        assertThat(termsFromResultQuery, containsInAnyOrder("firstTerm", "secondTerm"));

    }
}