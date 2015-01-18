package support;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static support.StringSplitterUtils.*;

public class StringSplitterUtilsUnitTest {

    @Test
    public void testFindOperationInString() throws Exception {

        final String searchString = "BMW 1er; Baujahr > 2006";

        final Set<String> operationResult = findOperation(searchString);

        assertThat(operationResult.iterator().next(), equalTo("Baujahr > 2006"));
    }


    @Test
    public void testFindMultipleOperationsInString() throws Exception {

        final String searchString = "BMW 1er; Baujahr > 2006; Baujahr < 2012";

        final Set<String> operationResult = findOperation(searchString);

        assertThat(operationResult, hasSize(2));
        assertThat(operationResult, containsInAnyOrder("Baujahr > 2006", "Baujahr < 2012"));

    }


    @Test
    public void testRemoveOperationsFromString() throws Exception {
        final String searchString = "BMW 1er; > 2006 Baujahr; < 2012 Baujahr";

        final String operationResult = removeOperation(searchString);

        assertThat(operationResult, equalTo("BMW 1er"));

    }

    @Test
    public void testCreateOperation() throws Exception {
        final String searchString = "BMW 1er; Baujahr > 2006; Baujahr < 2012";
        final Set<String> operationResult = findOperation(searchString);

        final Set<ComparisonOperation> operations = getComparisionOperationsFrom(searchString);

        assertThat(operations, hasSize(2));
    }

}