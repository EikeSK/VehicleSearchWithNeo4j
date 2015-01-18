package support;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VehicleSearchQueryGenerator {

    public static String generateCypherQueryFrom(final VehicleNodeSearchQuery searchQuery) {
        final List<String> terms = new ArrayList<>(searchQuery.getTerms());
        final List<ComparisonOperation> comparisonOperations = new ArrayList<>(searchQuery.getComparisonOperations());

        return createStartBlock(terms) +
                createMatchBlock(terms, comparisonOperations) +
                createWhereBlock(comparisonOperations) +
                createReturnBlock(comparisonOperations);
    }


    public static String generateCypherQueryForAutocompletion(final String incompleteTermName) {
        return "START n=node:terms(\"name:*" + incompleteTermName + "*\") MATCH (n:Term) RETURN n";
    }

    public static VehicleNodeSearchQuery generateSearchQueryFrom(final Set<String> tokens, final Set<ComparisonOperation> comparisonOperations) {
        return VehicleNodeSearchQuery.query()
                .withTerms(tokens)
                .withComparisonOperations(comparisonOperations);
    }


    private static String createReturnBlock(List<ComparisonOperation> comparisonOperations) {
        if (comparisonOperations.size() > 0) {
            return " RETURN DISTINCT(node)";
        } else {
            return " RETURN node";
        }
    }

    private static String createWhereBlock(List<ComparisonOperation> comparisonOperations) {
        StringBuilder sb = new StringBuilder();
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
        return sb.toString();
    }

    private static String createMatchBlock(List<String> terms, List<ComparisonOperation> comparisonOperations) {
        final List<String> uniqueComparisonUnits = getUniqueComparisonUnitsFrom(comparisonOperations);
        StringBuilder sb = new StringBuilder();
        sb.append(" MATCH");
        for (int i = 0; i < terms.size(); i++) {
            sb.append(" (_").append(createVariableFor(terms.get(i))).append(")-[:MATCHES_FOR]->(node)");
            if (comparisonOperations.size() > 0 || i < terms.size() - 1) {
                sb.append(",");
            }
        }
        for (int i = 0; i < uniqueComparisonUnits.size(); i++) {
            sb.append(" (_range_")
                    .append(comparisonOperations.get(i).getUnit())
                    .append(":")
                    .append(StringUtils.capitalize(comparisonOperations.get(i).getUnit()))
                    .append(")-[:MATCHES_FOR]->(node)");
            if (i < uniqueComparisonUnits.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private static List<String> getUniqueComparisonUnitsFrom(List<ComparisonOperation> comparisonOperations) {
        final List<String> uniqueUnits = new ArrayList<>();
        comparisonOperations.stream().filter(operation -> !uniqueUnits.contains(operation.getUnit())).forEach(operation -> {
            uniqueUnits.add(operation.getUnit());
        });
        return uniqueUnits;
    }

    private static String createStartBlock(List<String> terms) {
        final StringBuilder sb = new StringBuilder();
        sb.append("START");
        for (int i = 0; i < terms.size(); i++) {
            sb.append(" _").append(createVariableFor(terms.get(i))).append("=node:terms(\"name:*").append(terms.get(i)).append("*\")");
            if (i < terms.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
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
