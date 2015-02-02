package support;

import java.util.HashSet;
import java.util.Set;

/**
 * Repräsentiert ein Objekt, welches zusätzliche Metainformationen zu einem Fahrzeug beinhaltet.
 */
public class VehicleMetaData {

    private final Set<String> _additionalMetaData = new HashSet<>();
    private int _baujahrFrom;
    private int _baujahrTo;

    /**
     * Liefert ein Set von zusätzlichen Suchbegriffen
     *
     * @return ein Set von zusätzlichen Suchbegriffen
     */
    public Set<String> getAdditionalMetaData() {
        return _additionalMetaData;
    }

    /**
     * Setzt die zusätzlichen Suchbegriffe für ein Fahrzeug
     *
     * @param additionalMetaData die zusätzlichen Suchbegriffe für ein Fahrzeug
     */
    public void setAdditionalMetaData(final Set<String> additionalMetaData) {
        _additionalMetaData.addAll(additionalMetaData);
    }

    /**
     * Liefert das Baujahr, zu dem das Fahrzeug das erste Mal produziert wurde
     *
     * @return das Baujahr, zu dem das Fahrzeug das erste Mal produziert wurde
     */
    public int getBaujahrFrom() {
        return _baujahrFrom;
    }

    /**
     * Setzt das Baujahr, zu dem das Fahrzeug das erste Mal produziert wurde
     *
     * @param baujahrFrom das Baujahr, zu dem das Fahrzeug das erste Mal produziert wurde
     */
    public void setBaujahrFrom(final int baujahrFrom) {
        _baujahrFrom = baujahrFrom;
    }

    /**
     * Liefert das Baujahr, zu dem das Fahrzeug das letzte Mal produziert wurde.
     *
     * @return das Baujahr, zu dem das Fahrzeug das letzte Mal produziert wurde.
     */
    public int getBaujahrTo() {
        return _baujahrTo;
    }

    /**
     * Setzt das Baujahr, zu dem das Fahrzeug das letzte Mal produziert wurde.
     *
     * @param baujahrTo das Baujahr, zu dem das Fahrzeug das letzte Mal produziert wurde.
     */
    public void setBaujahrTo(int baujahrTo) {
        _baujahrTo = baujahrTo;
    }
}
