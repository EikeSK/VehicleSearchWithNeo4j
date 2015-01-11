package support;

import domain.ComparisonOperation;

import java.util.HashSet;
import java.util.Set;

public class VehicleNodeSearchQuery {

    private Set<String> _terms = new HashSet<>();
    private Set<ComparisonOperation> _comparisonOperations = new HashSet<>();

    public static VehicleNodeSearchQuery query() {
        return new VehicleNodeSearchQuery();
    }

    public VehicleNodeSearchQuery addTerm(final String term) {
        _terms.add(term);
        return this;
    }

    public VehicleNodeSearchQuery withTerms(final Set<String> terms) {
        _terms.addAll(terms);
        return this;
    }

    public VehicleNodeSearchQuery addComparisionOperation(final ComparisonOperation comparisonOperation) {
        _comparisonOperations.add(comparisonOperation);
        return this;
    }

    public Set<String> getTerms() {
        return _terms;
    }

    public Set<ComparisonOperation> getComparisonOperations() {
        return _comparisonOperations;
    }
}
