package support;

import com.google.common.base.Splitter;
import domain.VehicleNode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Ascii.toLowerCase;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.math.NumberUtils.toFloat;

/**
 * Eine Sammlung von statischen Methoden, die Operationen auf Strings ausführen.
 */
public class StringSplitterUtils {

    /**
     * Erzeugt aus dem Namen eines Fahrzeugknotens ein Set von Begriffen.
     * Die einzelnen Begriffe werden durch die Trennung mit Leerzeichen identifiert.
     *
     * @param vehicleNode Der Fahrzeugknoten, dessen Namen in einzelne Begriffe geteilt werden soll.
     * @return Die identifitierten Begriffe aus dem Namen des Fahrzeugknotens
     */
    public static Set<String> tokenize(final VehicleNode vehicleNode) {
        String toTokenize = null;
        if (vehicleNode != null) {
            toTokenize = vehicleNode.getName();
        }
        return tokenize(toTokenize);
    }

    /**
     * Teilt einen String in einzelne Begriffe. Die Begriffe müssen für die Identifizierung im String durch
     * Leerzeichen voneinander getrennt sein.
     * Die Ergebnisbegriffe weisen dabei ausschließlich Kleinbuchstaben auf.
     *
     * @param string String, der ein oder mehrere Begriffe getrennt durch Leerzeichen enthält.
     * @return Ein Set von Strings, welches die einzelnen Begriffe beinhaltet.
     */
    public static Set<String> tokenize(final String string) {
        final Set<String> tokens = new HashSet<>();
        if (string != null) {
            final Splitter splitter = Splitter.on(" ").omitEmptyStrings().trimResults();
            final Iterable<String> tokenIterable = splitter.split(string);
            for (String token : tokenIterable) {
                tokens.add(toLowerCase(token));
            }
        }
        return tokens;
    }

    /**
     * Liefert anhand eines Set von Strings Objekte vom Typ ComparisonOperation. Ein String des Sets beinhaltet dabei
     * an erster Stelle die Einheit, gefolgt vom Operatior sowie vom zu vergleichenden Wert. Die Angaben werden dabei
     * durch Leerzeichen voneinander getrennt
     * <br><br>
     * Beispiel: <code>baujahr &gt; 2006</code><br>
     * Syntax: <code>{einheit}{operator}{wert}</code>
     * <br><br>
     * Entspricht der String nicht der erwarteten Syntax, wird kein repräsentierendes Objekt erstellt.
     *
     * @param operations Ein Set mit Strings, welche die nötigen Angaben für eine Vergleichsoperation beinhalten.
     * @return Ein Set von Objekten vom Typ ComparisonOperations, die anhand valider Strings erzeugt werden konnten.
     */
    public static Set<ComparisonOperation> getComparisionOperationsFrom(final Set<String> operations) {
        final Set<ComparisonOperation> operationsOnQueries = new HashSet<>();
        for (String singleOperation : operations) {
            final List<String> strings = Splitter.on(" ").splitToList(singleOperation);
            if (strings.size() == 3) {
                final Operator operator = Operator.findByOperation(strings.get(1));
                if (operator != null) {
                    operationsOnQueries.add(new ComparisonOperation(strings.get(0), operator, toFloat(strings.get(2))));
                }
            }
        }
        return operationsOnQueries;
    }

    /**
     * Entfernt aus einem Suchstring die Angaben von Vergleichsoperationen.
     * <br>
     * <b>Beispiel:</b><br>
     * vorher: <code>Audi A4; baujahr &gt; 2004; baujahr %lt; 2010</code><br>
     * nachher: <code>Audi A4</code>
     *
     * @param searchString Der von Vergleichsoperationen zu bereinigende String
     * @return ein String, der nur die Suchbegriffe enthält.
     */
    public static String removeOperation(String searchString) {
        return StringUtils.substringBefore(searchString, ";");
    }

    public static Set<String> findOperation(String searchString) {
        final Set<String> result = new HashSet<>();
        final String operationString = StringUtils.substringAfter(searchString, ";");
        final Iterable<String> split = Splitter.on(";").trimResults().split(operationString);
        for (String resultToken : split) {
            if (isNotBlank(resultToken)) {
                result.add(resultToken);
            }
        }
        return result;
    }
}
