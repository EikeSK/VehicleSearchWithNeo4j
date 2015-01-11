package service;

import com.google.common.base.Splitter;
import domain.NodeMetaData;
import domain.ComparisonOperation;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

public class ManualTest {

    @Test
    public void testFindOperationInString() throws Exception {

        final String searchString = "BMW 1er; > 2006 Baujahr";

        final Set<String> operationResult = findOperation(searchString);

        assertThat(operationResult.iterator().next(), equalTo("> 2006 Baujahr"));

    }


    @Test
    public void testFindOperationInString2() throws Exception {

        final String searchString = "BMW 1er; > 2006 Baujahr; < 2012 Baujahr";

        final Set<String> operationResult = findOperation(searchString);

        assertThat(operationResult, hasSize(2));
        assertThat(operationResult, containsInAnyOrder("> 2006 Baujahr", "< 2012 Baujahr"));

    }

    @Test
    public void testRemoveOperationsFromString() throws Exception {
        final String searchString = "BMW 1er; > 2006 Baujahr; < 2012 Baujahr";

        final String operationResult = removeOperation(searchString);

        assertThat(operationResult, equalTo("BMW 1er"));

    }

    @Test
    public void testCreateOperation() throws Exception {
        final String searchString = "BMW 1er; > 2006 Baujahr; < 2012 Baujahr";
        final Set<String> operationResult = findOperation(searchString);

        final Set<ComparisonOperation> operations = getComparisionOperationsFrom(searchString);
    }

    private Set<ComparisonOperation> getComparisionOperationsFrom(String searchString) {
        final Set<String> operations = findOperation(searchString);
        final Set<ComparisonOperation> operationsOnQueries = new HashSet<>();
        for (String singleOperation : operations) {
            final List<String> strings = Splitter.on(" ").splitToList(singleOperation);
            if (strings.size() == 3) {
                final Operator operator = Operator.findByOperation(strings.get(0));
                if (operator != null) {
                    final NodeMetaData nodeMetaData = new NodeMetaData();
                    nodeMetaData.setName(strings.get(1));
                    nodeMetaData.setUnit(strings.get(2));
                    operationsOnQueries.add(new ComparisonOperation(operator, nodeMetaData));
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
        final Iterable<String> split = Splitter.on(";").trimResults().split(searchString);
        for (String resultToken : split) {
            if (resultToken.charAt(1) == ' ')
            result.add(resultToken);
        }
        return result;
    }


}


