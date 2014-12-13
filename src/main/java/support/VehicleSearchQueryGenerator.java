package support;

import static com.google.common.base.Ascii.toLowerCase;

public class VehicleSearchQueryGenerator {

    public String generateCypherQueryFrom(final VehicleModelSearchQuery searchQuery) {
        final StringBuilder sb = new StringBuilder();
        if (searchQuery.getStartTerm() != null) {
            sb.append("START a=node:terms(name=").append(searchQuery.getStartTerm()).append(") ");
        }

        if (searchQuery.getTerms().size() > 0 && searchQuery.getStartTerm() != null) {
            sb.append("MATCH");
            sb.append("(a)-[:MATCHES_FOR]->(modell)");
        }
        for (String term : searchQuery.getTerms()) {
            sb.append(", ");
            sb.append("(").append(toLowerCase(term)).append(":{name:'").append(term).append("'})-[:MATCHES_FOR}->(modell)");
        }
        sb.append("RETURN modell");

        return sb.toString();
    }
}
