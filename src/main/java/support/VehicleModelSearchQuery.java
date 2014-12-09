package support;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Ascii.toLowerCase;

public class VehicleModelSearchQuery {

    private String _startTerm;
    private Set<String> _terms = new HashSet<>();

    public static VehicleModelSearchQuery query() {
        return new VehicleModelSearchQuery();
    }

    public VehicleModelSearchQuery withStartTerm(final String startTerm) {
        _startTerm = startTerm;
        return this;
    }

    public VehicleModelSearchQuery withTerm(final String term) {
        _terms.add(term);
        return this;
    }

    public String toCypherQuery() {
        final StringBuilder sb = new StringBuilder();
        if (_startTerm != null) {
            sb.append("START a=node:terms(name=").append(_startTerm).append(") ");
        }

        if (_terms.size() > 0 && _startTerm != null) {
            sb.append("MATCH");
            sb.append("(a)-[:MATCHES_FOR]->(modell)");
        }
        for (String term : _terms) {
            sb.append(", ");
            sb.append("(").append(toLowerCase(term)).append(":{name:'").append(term).append("'})-[:MATCHES_FOR}->(modell)");
        }
        sb.append("RETURN modell");

        return sb.toString();
    }
}
