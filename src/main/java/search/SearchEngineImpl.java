package search;

import domain.VehicleNode;
import org.springframework.stereotype.Component;
import repositories.TermService;
import repositories.VehicleNodeService;
import support.StringSplitterUtils;
import support.VehicleNodeSearchQuery;
import support.VehicleSearchQueryGenerator;

import java.util.Collection;
import java.util.Set;

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
        final Set<String> tokensFromSearchString = StringSplitterUtils.tokenize(searchString);
        final VehicleNodeSearchQuery vehicleNodeSearchQuery = VehicleSearchQueryGenerator.generateSearchQueryFrom(tokensFromSearchString);
        return _vehicleNodeService.findNodesByQuery(vehicleNodeSearchQuery);
    }

    @Override
    public Collection<String> autocomplete(String searchTerm) {
        return _termService.findTermNamesByIncompleteName(searchTerm);
    }
}
