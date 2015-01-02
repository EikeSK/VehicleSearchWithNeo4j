package search;

import domain.VehicleNode;
import org.springframework.stereotype.Component;
import repositories.VehicleNodeService;
import support.StringSplitterUtils;
import support.VehicleNodeSearchQuery;
import support.VehicleSearchQueryGenerator;

import java.util.Collection;
import java.util.Set;

@Component
public class SearchEngineImpl implements SearchEngine {

    private final VehicleNodeService _vehicleNodeService;

    public SearchEngineImpl(final VehicleNodeService vehicleNodeService) {
        _vehicleNodeService = vehicleNodeService;
    }

    @Override
    public Collection<VehicleNode> search(final String searchString) {
        final Set<String> tokensFromSearchString = StringSplitterUtils.tokenize(searchString);
        final VehicleNodeSearchQuery vehicleNodeSearchQuery = VehicleSearchQueryGenerator.generateSearchQueryFrom(tokensFromSearchString);
        return _vehicleNodeService.findNodesByQuery(vehicleNodeSearchQuery);
    }
}
