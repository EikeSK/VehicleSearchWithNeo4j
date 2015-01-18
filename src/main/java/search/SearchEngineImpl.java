package search;

import domain.VehicleNode;
import org.springframework.stereotype.Component;
import repositories.TermService;
import repositories.VehicleNodeService;
import support.ComparisonOperation;
import support.VehicleNodeSearchQuery;
import support.VehicleSearchQueryGenerator;

import java.util.Collection;
import java.util.Set;

import static support.StringSplitterUtils.*;

@Component
public class SearchEngineImpl implements SearchEngine {

    private final VehicleNodeService _vehicleNodeService;
    private final TermService _termService;

    public SearchEngineImpl(final VehicleNodeService vehicleNodeService, TermService termService) {
        _vehicleNodeService = vehicleNodeService;
        _termService = termService;
    }

    @Override
    public Collection<VehicleNode> search(final String searchString) {
        final Set<ComparisonOperation> comparisionOperations = getComparisionOperationsFrom(searchString);
        final Set<String> tokensFromSearchString = tokenize(removeOperation(searchString));
        final VehicleNodeSearchQuery vehicleNodeSearchQuery = VehicleSearchQueryGenerator.generateSearchQueryFrom(tokensFromSearchString, comparisionOperations);
        return _vehicleNodeService.findNodesByQuery(vehicleNodeSearchQuery);
    }

    @Override
    public Collection<String> autocomplete(String searchTerm) {
        return _termService.findTermNamesByIncompleteName(searchTerm);
    }
}
