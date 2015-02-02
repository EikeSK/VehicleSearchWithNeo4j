package support;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Repräsentiert einen Generator, der Methoden für das Erstellen einer Suchanfrage anhand eines Suchstrings und
 * das Erstellen von Cypher-Anfragen bereitstellt.
 */
public class VehicleSearchQueryGenerator {

    /**
     * Erzeugt anhand eines Objektes vom Typ VehicleNodeSearchQuery eine Abfrage in Cypher als String.
     *
     * @param searchQuery Die Suchabfrage, für die eine Cypher-Abfrage generiert werden soll.
     * @return die generierte Cypher-Abfrage als String
     */
    public static String generateCypherQueryFrom(final VehicleNodeSearchQuery searchQuery) {
        final List<String> terms = new ArrayList<>(searchQuery.getTerms());
        final List<ComparisonOperation> comparisonOperations = new ArrayList<>(searchQuery.getComparisonOperations());

        return createStartBlock(terms) +
                createMatchBlock(terms, comparisonOperations) +
                createWhereBlock(comparisonOperations) +
                createReturnBlock(comparisonOperations);
    }


    /**
     * Erzeugt anhand eines Begriffs eine Cypher-Abfrage für Autovervollständigung.
     *
     * @param incompleteTermName Der unvollständige Suchbegriff, für den eine Autocomplete-Abfrage in Cypher erzeugt werden soll
     * @return die generierte Cypher-Abfrage als String.
     */
    public static String generateCypherQueryForAutocompletion(final String incompleteTermName) {
        return "START n=node:terms(\"name:" + incompleteTermName + "*\") MATCH (n:Term) RETURN n";
    }

    /**
     * Erzeugt anhand von Suchbegriffen und Vergleichsoperationen ein vom Typ VehicleNodeSearchQuery zur Repräsentation einer Suchanfrage
     *
     * @param tokens               Eine Liste von Suchbegriffen, die in der Suchanfrage enthalten sein sollen.
     * @param comparisonOperations Eine Liste von Vergleichsoperationen, die in der Suchanfrage enthalten sein sollen.
     * @return ein Objekt vom Typ VehicleNodeSearchQuery, welches die angegebenen Suchbegriffe und Vergleichsoperationen enthält.
     */
    public static VehicleNodeSearchQuery generateSearchQueryFrom(final Set<String> tokens, final Set<ComparisonOperation> comparisonOperations) {
        return VehicleNodeSearchQuery.query()
                .withTerms(tokens)
                .withComparisonOperations(comparisonOperations);
    }

    /**
     * Erzeugt den RETURN-Block einer Cypher-Abfrage.
     * Beinhaltet die Abfrage Vergleichsoperationen, werden die Ergebnisse mit <code>DISTINCT</code> gefiltert
     *
     * @param comparisonOperations Liste von Vergleichsoperationen.
     * @return der RETURN-Block einer Cypher-Abfrage
     */
    private static String createReturnBlock(List<ComparisonOperation> comparisonOperations) {
        if (comparisonOperations.size() > 0) {
            return " RETURN DISTINCT(node)";
        } else {
            return " RETURN node";
        }
    }

    /**
     * Erzeugt den WHERE-Block einer Cypher-Abfrage.
     *
     * @param comparisonOperations Liste von einzubeziehenden Vergleichsoperationen
     * @return der WHERE-Block einer Cypher-Abfrage
     */
    private static String createWhereBlock(List<ComparisonOperation> comparisonOperations) {
        StringBuilder sb = new StringBuilder();
        if (comparisonOperations.size() > 0) {
            sb.append(" WHERE");
            for (int i = 0; i < comparisonOperations.size(); i++) {
                if (i > 0) {
                    sb.append(" AND");
                }
                sb.append(" _range_").append(comparisonOperations.get(i).getUnit()).append(".value ")
                        .append(comparisonOperations.get(i).getOperator().getOperation())
                        .append(" ")
                        .append(getValueAsString(comparisonOperations.get(i).getValue()));
            }
        }
        return sb.toString();
    }

    /**
     * Erzeugt den CREATE-Block einer Cypher-Abfrage
     *
     * @param terms                die beim Erzeugen einzubeziehenden Suchbegriffe
     * @param comparisonOperations die beum Erzeugen einzubeziehenden Vergleichsoperationen
     * @return der CREATE-Block einer Cypher-Abfrage
     */
    private static String createMatchBlock(List<String> terms, List<ComparisonOperation> comparisonOperations) {
        final List<String> uniqueComparisonUnits = getUniqueComparisonUnitsFrom(comparisonOperations);
        StringBuilder sb = new StringBuilder();
        sb.append(" MATCH");
        for (int i = 0; i < terms.size(); i++) {
            sb.append(" (_").append(createVariableFor(terms.get(i))).append(")-[:MATCHES_FOR]->(node)");
            if (comparisonOperations.size() > 0 || i < terms.size() - 1) {
                sb.append(",");
            }
        }
        for (int i = 0; i < uniqueComparisonUnits.size(); i++) {
            sb.append(" (_range_")
                    .append(comparisonOperations.get(i).getUnit())
                    .append(":")
                    .append(StringUtils.capitalize(comparisonOperations.get(i).getUnit()))
                    .append(")-[:MATCHES_FOR]->(node)");
            if (i < uniqueComparisonUnits.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Liefert anhand einer Liste von Objekte des Typs ComparisonOperation alle verwendeteten Einheiten.
     * In der Ergebnisliste wird jede Einheit einzigartig
     *
     * @param comparisonOperations Liste mit Vergleichsoperationen
     * @return Liste mit einzigartigen Einheiten in Form von Strings aus den Vergleichsoperationen
     */
    private static List<String> getUniqueComparisonUnitsFrom(List<ComparisonOperation> comparisonOperations) {
        final List<String> uniqueUnits = new ArrayList<>();
        comparisonOperations
                .stream()
                .filter(operation -> !uniqueUnits.contains(operation.getUnit()))
                .forEach(operation -> uniqueUnits.add(operation.getUnit()));
        return uniqueUnits;
    }

    /**
     * Erzeugt den START-Block einer Cypher-Abfrage
     *
     * @param terms die beim Erzeugen einzubeziehenden Suchbegriffe
     * @return der START-Block einer Cypher-Abfrage
     */
    private static String createStartBlock(List<String> terms) {
        final StringBuilder sb = new StringBuilder();
        sb.append("START");
        for (int i = 0; i < terms.size(); i++) {
            sb.append(" _").append(createVariableFor(terms.get(i))).append("=node:terms(\"name:*").append(terms.get(i)).append("*\")");
            if (i < terms.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Konvertiert einen Double-Wert in einen String.
     * Dabei werden ganzzahlige Werte wie 2000.0 in "2000" umgewandelt.
     *
     * @param value der zu konvertierende Wert
     * @return der konvertierte Wert als String
     */
    private static String getValueAsString(double value) {
        String valueAsString;
        if (value % 1 == 0) {
            valueAsString = String.valueOf((int) value);
        } else {
            valueAsString = String.valueOf(value);
        }
        return valueAsString;
    }

    /**
     * Ersetzt einen Punkt durch die Zeichenkette "dot", so werden aus Suchbegriffen valide Cypher-Variablen erzeugt.
     *
     * @param term der in eine Cypher-Variable umzuwandelne Begriff
     * @return der Begriff als valide Cypher-Variable
     */
    private static String createVariableFor(String term) {
        return StringUtils.replace(term, ".", "dot");
    }
}
