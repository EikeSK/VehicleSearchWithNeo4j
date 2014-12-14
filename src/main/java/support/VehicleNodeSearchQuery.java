package support;

import java.util.HashSet;
import java.util.Set;

public class VehicleNodeSearchQuery {

    private String _startTerm;
    private Set<String> _terms = new HashSet<>();

    public static VehicleNodeSearchQuery query() {
        return new VehicleNodeSearchQuery();
    }

    public VehicleNodeSearchQuery withStartTerm(final String startTerm) {
        _startTerm = startTerm;
        return this;
    }

    public VehicleNodeSearchQuery withTerm(final String term) {
        _terms.add(term);
        return this;
    }

    public String getStartTerm() {
        return _startTerm;
    }

    public Set<String> getTerms() {
        return _terms;
    }
}
