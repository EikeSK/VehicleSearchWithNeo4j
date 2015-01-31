package search;

import domain.VehicleNode;

import java.util.Collection;

/**
 * Repräsentiert die Schnittstelle für das Ausführen einer Suche.
 */
public interface SearchEngine {

    /**
     * Führt anhand einer Zeichenkette, die ein oder mehrere Suchbegriffe sowie Angaben von Wertebereichen für
     * entsprechende Knoten besitzten kann, eine Suche aus.
     * Bei der Angabe mehrerer Suchbegriffe müssen diese durch Leerzeichen voneinander getrennt sein.
     * Beispiel: <code>Audi A4 B6 Kombi</code>
     * <br>
     * Für die Angabe von Wertebereichen, z.B. für Baujahre, gilt folgende Syntax:
     * <code>{Suchbegriff};{Einheitenname}{Operator}{Wert}</code>
     * <br>
     * Wertebereiche werden durch ein Semikolon eingeleitet. Der Suchbegriff kann um beliebig viele Wertebereiche erweitert werden.
     * Beispiel: <code>Audi; Baujahr &gt;  2006; Baujahr &lt;  2010</code>
     * Für die Suche nach Wertebereichen muss mindestens ein Suchbegriff vorhanden sein.
     * <br>
     * Sind eingegebene Suchbegriffe unvollständig, so werden Fahrzeuge als Ergebnis geliefert, die den angegebenen Suchbegriff
     * als Zeichenkette enthalten.
     * Bei falscher Syntax ist das Ergebnis leer.
     *
     * @param searchString Zeichkette, die ein oder mehrere Suchbegriffe sowie Angaben von Wertebereichen entahlten kann.
     * @return Die für die Zeichenkette gefundenen Fahrzeugknoten als Liste, sofern welche gefunden werden und die
     * angegebene Syntax in der Such-Zeichenkette korrekt  war.
     */
    public Collection<VehicleNode> search(String searchString);


    /**
     * Liefert anhand einer Zeichenkette alle Suchbegriffe als Liste von Strings, die mit dem angegebenen Begriff beginnen
     *
     * @param searchTerm Der Begriff, für den vervollständigte Begriffe aus der Datenbank geladen werden sollen
     * @return Eine Liste von vervollständigten Begriffen. Wurden keine passenden Begriffe gefunden, ist die Liste leer.
     */
    public Collection<String> autocomplete(String searchTerm);
}
