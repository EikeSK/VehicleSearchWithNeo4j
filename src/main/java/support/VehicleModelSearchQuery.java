package support;

import java.util.HashSet;
import java.util.Set;

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

    public String getStartTerm() {
        return _startTerm;
    }

    public Set<String> getTerms() {
        return _terms;
    }
}
