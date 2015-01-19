package support;

import com.google.common.base.Splitter;
import domain.VehicleNode;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Ascii.toLowerCase;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.math.NumberUtils.toFloat;

public class StringSplitterUtils {

    public static Set<String> tokenize(final VehicleNode vehicleNode) {
        String toTokenize = null;
        if (vehicleNode != null) {
            toTokenize = vehicleNode.getName();
        }
        return tokenize(toTokenize);
    }

    public static Set<String> tokenize(final String string) {
        final Set<String> tokens = new HashSet<>();
        if (string != null) {
            final Splitter splitter = Splitter.on(" ").omitEmptyStrings().trimResults();
            final Iterable<String> tokenIterable = splitter.split(string);
            for (String token : tokenIterable) {
                tokens.add(toLowerCase(token));
            }
        }
        return tokens;
    }

    public static Set<ComparisonOperation> getComparisionOperationsFrom(final Set<String> operations) {
        final Set<ComparisonOperation> operationsOnQueries = new HashSet<>();
        for (String singleOperation : operations) {
            final List<String> strings = Splitter.on(" ").splitToList(singleOperation);
            if (strings.size() == 3) {
                final Operator operator = Operator.findByOperation(strings.get(1));
                if (operator != null) {
                    operationsOnQueries.add(new ComparisonOperation(strings.get(0), operator, toFloat(strings.get(2))));
                }
            }
        }
        return operationsOnQueries;
    }

    public static String removeOperation(String searchString) {
        return StringUtils.substringBefore(searchString, ";");
    }

    public static Set<String> findOperation(String searchString) {
        final Set<String> result = new HashSet<>();
        final String operationString = StringUtils.substringAfter(searchString, ";");
        final Iterable<String> split = Splitter.on(";").trimResults().split(operationString);
        for (String resultToken : split) {
            if (isNotBlank(resultToken))
            result.add(resultToken);
        }
        return result;
    }
}
