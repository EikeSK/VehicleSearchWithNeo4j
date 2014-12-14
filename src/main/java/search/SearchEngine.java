package search;

import domain.VehicleNode;
import support.StringSplitterUtils;
import support.VehicleNodeSearchQuery;
import support.VehicleSearchQueryGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class SearchEngine {

    private final VehicleSearchQueryGenerator _vehicleSearchQueryGenerator;

    public SearchEngine(VehicleSearchQueryGenerator vehicleSearchQueryGenerator) {
        _vehicleSearchQueryGenerator = vehicleSearchQueryGenerator;
    }

    public Collection<VehicleNode> search(final String searchString) {
        final Collection<VehicleNode> searchResults = new ArrayList<>();
        final Set<String> tokensFromSearchString = StringSplitterUtils.tokenize(searchString);
        final VehicleNodeSearchQuery vehicleNodeSearchQuery = _vehicleSearchQueryGenerator.generateSearchQueryFrom(tokensFromSearchString);
        return searchResults;
    }
}
