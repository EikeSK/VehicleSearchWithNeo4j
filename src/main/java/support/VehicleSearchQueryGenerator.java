package support;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VehicleSearchQueryGenerator {

    public static String generateCypherQueryFrom(final VehicleNodeSearchQuery searchQuery) {
        final StringBuilder sb = new StringBuilder();
        final List<String> terms = new ArrayList<>(searchQuery.getTerms());

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
            if (i < terms.size() - 1) {
                sb.append(",");
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

    private static String createVariableFor(String term) {
        return StringUtils.replace(term, ".", "dot");
    }
}
