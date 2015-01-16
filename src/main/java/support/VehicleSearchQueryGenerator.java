package support;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VehicleSearchQueryGenerator {

    // TODO: aufteilen in generateSTARTClause, generateMATCHClause, generateWHEREclause, generateRETURNClause
    public static String generateCypherQueryFrom(final VehicleNodeSearchQuery searchQuery) {
        final StringBuilder sb = new StringBuilder();
        final List<String> terms = new ArrayList<>(searchQuery.getTerms());
        final List<ComparisonOperation> comparisonOperations = new ArrayList<>(searchQuery.getComparisonOperations());

        sb.append("START");
        for (int i = 0; i < terms.size(); i++) {
            sb.append(" _").append(createVariableFor(terms.get(i))).append("=node:terms(\"name:*").append(terms.get(i)).append("*\")");
            if (i < terms.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(" MATCH");
        for (int i = 0; i < terms.size(); i++) {
            sb.append(" (_").append(createVariableFor(terms.get(i))).append(")-[:MATCHES_FOR]->(node)");
            if (comparisonOperations.size() > 0 || i < terms.size() - 1) {
                sb.append(",");
            }
        }
        for (int i = 0; i < comparisonOperations.size(); i++) {
            sb.append(" (_range_").append(comparisonOperations.get(i).getUnit()).append(")-[:MATCHES_FOR]->(node)");
            if (i < comparisonOperations.size() - 1) {
                sb.append(",");
            }
        }
        if (comparisonOperations.size() > 0) {
            sb.append(" WHERE");
            for (int i = 0; i < comparisonOperations.size(); i++) {
                if (i > 0) {
                    sb.append(" AND");
                }
                sb.append(" _range_").append(comparisonOperations.get(i).getUnit()).append(".value ")
                        .append(comparisonOperations.get(i).getOperator().getOperation())
                        .append(" ")
                        .append(getValueAsString(comparisonOperations.get(i).getValue()));
            }
        }

        sb.append(" RETURN node");

        return sb.toString();
    }

    public static String generateCypherQueryForAutocompletion(final String incompleteTermName) {
        final StringBuilder sb = new StringBuilder();

        sb.append("START n=node:terms(\"name:*")
                .append(incompleteTermName)
                .append("*\") MATCH (n:Term) RETURN n");

        return sb.toString();
    }

    public static VehicleNodeSearchQuery generateSearchQueryFrom(final Set<String> tokens) {
        final Set<String> terms = tokens.stream().collect(Collectors.toSet()); // TODO: überflüssig?

        return VehicleNodeSearchQuery.query().withTerms(terms);
    }

    // http://stackoverflow.com/questions/12045137/set-number-of-decimal-places-to-0-if-float-is-an-integer-java
    private static String getValueAsString(double value) {
        String valueAsString;
        if (value % 1 == 0) {
            valueAsString = String.valueOf((int) value);
        } else {
            valueAsString = String.valueOf(value);
        }
        return valueAsString;
    }

    private static String createVariableFor(String term) {
        return StringUtils.replace(term, ".", "dot");
    }
}
