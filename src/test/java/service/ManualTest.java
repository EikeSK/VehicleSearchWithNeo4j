package service;

import com.google.common.base.Splitter;
import domain.ComparisonOperation;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import support.Operator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang.math.NumberUtils.toFloat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

public class ManualTest {

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

    private Set<ComparisonOperation> getComparisionOperationsFrom(String searchString) {
        final Set<String> operations = findOperation(searchString);
        final Set<ComparisonOperation> operationsOnQueries = new HashSet<>();
        for (String singleOperation : operations) {
            final List<String> strings = Splitter.on(" ").splitToList(singleOperation);
            if (strings.size() == 3) {
                final Operator operator = Operator.findByOperation(strings.get(1));
                if (operator != null) {
                    operationsOnQueries.add(new ComparisonOperation(operator, strings.get(0), toFloat(strings.get(2))));
                }
            }
        }
        return operationsOnQueries;
    }

    private String removeOperation(String searchString) {
        return StringUtils.substringBefore(searchString, ";");
    }

    private Set<String> findOperation(String searchString) {
        final Set<String> result = new HashSet<>();
        final String operationString = StringUtils.substringAfter(searchString, ";");
        final Iterable<String> split = Splitter.on(";").trimResults().split(operationString);
        for (String resultToken : split) {
            result.add(resultToken);
        }
        return result;
    }

}


