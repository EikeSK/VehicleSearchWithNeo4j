package support;

import java.util.Set;

import static com.google.common.base.Ascii.toLowerCase;

public class VehicleSearchQueryGenerator {

    public String generateCypherQueryFrom(final VehicleNodeSearchQuery searchQuery) {
        final StringBuilder sb = new StringBuilder();
        final String startTerm = searchQuery.getStartTerm();
        final Set<String> terms = searchQuery.getTerms();
        if (startTerm != null) {
            sb.append("START a=node:terms(name='").append(startTerm).append("') ");
            sb.append("MATCH ");
            sb.append("(a)-[:MATCHES_FOR]->(modell)");
        }
        if (terms.size() > 0 && startTerm == null) {
            sb.append("MATCH ");
        }
        if (terms.size() == 1 && startTerm == null) {
            for (String term : terms) {
                sb.append("(").append(toLowerCase(term)).append(": {name:'").append(term).append("'})-[:MATCHES_FOR]->(modell)");
            }
        } else {
            for (String term : terms) {
                sb.append(", ");
                sb.append("(").append(toLowerCase(term)).append(": {name:'").append(term).append("'})-[:MATCHES_FOR]->(modell)");
            }
        }

        sb.append(" RETURN modell");

        return sb.toString();
    }
}
