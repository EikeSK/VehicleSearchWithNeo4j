package search;

import domain.VehicleNode;
import org.springframework.transaction.annotation.Transactional;
import repositories.VehicleNodeService;
import support.StringSplitterUtils;
import support.VehicleNodeSearchQuery;
import support.VehicleSearchQueryGenerator;

import java.util.Collection;
import java.util.Set;

public class SearchEngine {

    private final VehicleSearchQueryGenerator _vehicleSearchQueryGenerator;
    private final VehicleNodeService _vehicleNodeService;

    public SearchEngine(VehicleSearchQueryGenerator vehicleSearchQueryGenerator, VehicleNodeService vehicleNodeService) {
        _vehicleSearchQueryGenerator = vehicleSearchQueryGenerator;
        _vehicleNodeService = vehicleNodeService;
    }

    @Transactional
    public Collection<VehicleNode> search(final String searchString) {
        final Set<String> tokensFromSearchString = StringSplitterUtils.tokenize(searchString);
        final VehicleNodeSearchQuery vehicleNodeSearchQuery = _vehicleSearchQueryGenerator.generateSearchQueryFrom(tokensFromSearchString);
        return _vehicleNodeService.findNodesByQuery(vehicleNodeSearchQuery);
    }
}
