package search;

import domain.VehicleNode;
import org.springframework.stereotype.Component;
import service.TermService;
import service.VehicleNodeService;
import support.ComparisonOperation;
import support.VehicleNodeSearchQuery;
import support.VehicleSearchQueryGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static support.StringSplitterUtils.*;

/**
 * Repräsentiert die Implementierung der Schnittstelle für das Ausführen einer Suche.
 */
@Component
public class SearchEngineImpl implements SearchEngine {

    private static final String REGEX_NOT_ALLOWED_CHARACTERS_FOR_TERMS = ".*[^\\w\\.\\süäöß].*";

    private final VehicleNodeService _vehicleNodeService;
    private final TermService _termService;

    public SearchEngineImpl(final VehicleNodeService vehicleNodeService, TermService termService) {
        _vehicleNodeService = vehicleNodeService;
        _termService = termService;
    }

    @Override
    public Collection<VehicleNode> search(final String searchString) {
        Collection<VehicleNode> result = new ArrayList<>();
        final String cleanedSearchString = searchString.replaceAll("[^\\w<>=;.]", " ");
        final Set<String> operationsAsString = findOperation(cleanedSearchString);
        final Set<ComparisonOperation> comparisionOperations = getComparisionOperationsFrom(operationsAsString);
        final String searchStringWithoutOperations = removeOperation(cleanedSearchString);
        final Set<String> tokensFromSearchString = tokenize(searchStringWithoutOperations);

        if (tokensFromSearchString.size() > 0 && !containsIllegalCharacters(searchStringWithoutOperations) && operationsAsString.size() == comparisionOperations.size()) {
            final VehicleNodeSearchQuery vehicleNodeSearchQuery = VehicleSearchQueryGenerator.generateSearchQueryFrom(tokensFromSearchString, comparisionOperations);
            result = _vehicleNodeService.findNodesByQuery(vehicleNodeSearchQuery);
        }

        return result;
    }

    @Override
    public Collection<String> autocomplete(String searchTerm) {
        return _termService.findTermNamesByIncompleteName(searchTerm);
    }

    private boolean containsIllegalCharacters(String searchStringWithoutOperations) {
        return searchStringWithoutOperations.matches(REGEX_NOT_ALLOWED_CHARACTERS_FOR_TERMS);
    }
}
