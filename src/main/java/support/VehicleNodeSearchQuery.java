package support;

import java.util.HashSet;
import java.util.Set;

/**
 * Repäsentiert eine Suchanfrage an die Datenbank.
 */
public class VehicleNodeSearchQuery {

    private final Set<String> _terms = new HashSet<>();
    private final Set<ComparisonOperation> _comparisonOperations = new HashSet<>();

    /**
     * Erzeugt ein neues Objekt vom Typ VehicleNodeSearchQuery
     *
     * @return ein neues Objekt vom Typ VehicleNodeSearchQuery
     */
    public static VehicleNodeSearchQuery query() {
        return new VehicleNodeSearchQuery();
    }

    /**
     * Fügt der Anfrage einen Suchbegriff hinzu
     *
     * @param term ein Suchbegriff, der der Anfrage hinzugefügt werden soll
     * @return das Objekt dieser Klasse, ergänzt um den hinzugefügten Suchbegriff
     */
    public VehicleNodeSearchQuery addTerm(final String term) {
        _terms.add(term);
        return this;
    }

    /**
     * Setzt für die Anfrage eine Liste von Suchbegriffen
     *
     * @param terms eine Liste von Suchbegriffen
     * @return das Objekt dieser Klasse, ergänzt um die Liste von Suchbegriffen
     */
    public VehicleNodeSearchQuery withTerms(final Set<String> terms) {
        _terms.addAll(terms);
        return this;
    }

    /**
     * Fügt der Anfrage eine Vergleichsoperation hinzu.
     *
     * @param comparisonOperation eine Vergleichsoperation, die der Anfrage hinzugefügt werden soll
     * @return das Objekt dieser Klasse, ergänzt um die Liste von Suchbegriffen
     */
    public VehicleNodeSearchQuery addComparisonOperations(final ComparisonOperation comparisonOperation) {
        _comparisonOperations.add(comparisonOperation);
        return this;
    }

    /**
     * Settz für die Anfrage eine Liste von Vergleichsoperationen.
     *
     * @param comparisonOperations eine Liste von Vergleichsoperationen
     * @return das Objekt dieser Klasse, ergänzt um die Liste von Vergleichsoperationen.
     */
    public VehicleNodeSearchQuery withComparisonOperations(final Set<ComparisonOperation> comparisonOperations) {
        _comparisonOperations.addAll(comparisonOperations);
        return this;
    }

    /**
     * Liefert ein Set der Suchbegriffe dieser Abfrage
     *
     * @return ein Set der Suchbegriffe dieser Abfrage
     */
    public Set<String> getTerms() {
        return _terms;
    }

    /**
     * Liefert ein Set der Vergleichsoperationen dieser Anfrage
     *
     * @return ein Set der Vergleichsoperationen dieser Anfrage
     */
    public Set<ComparisonOperation> getComparisonOperations() {
        return _comparisonOperations;
    }
}
