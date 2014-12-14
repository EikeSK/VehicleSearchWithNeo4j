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

    public VehicleNodeSearchQuery addTerm(final String term) {
        _terms.add(term);
        return this;
    }

    public VehicleNodeSearchQuery withTerms(final Set<String> terms) {
        _terms.addAll(terms);
        return this;
    }

    public String getStartTerm() {
        return _startTerm;
    }

    public Set<String> getTerms() {
        return _terms;
    }
}
