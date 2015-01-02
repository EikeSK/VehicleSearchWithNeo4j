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
public class SearchEngine {

    private final VehicleNodeService _vehicleNodeService;

    public SearchEngine(final VehicleNodeService vehicleNodeService) {
        _vehicleNodeService = vehicleNodeService;
    }

    public Collection<VehicleNode> search(final String searchString) {
        final Set<String> tokensFromSearchString = StringSplitterUtils.tokenize(searchString);
        final VehicleNodeSearchQuery vehicleNodeSearchQuery = VehicleSearchQueryGenerator.generateSearchQueryFrom(tokensFromSearchString);
        return _vehicleNodeService.findNodesByQuery(vehicleNodeSearchQuery);
    }
}
