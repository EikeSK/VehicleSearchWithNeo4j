package support;

/**
 * Repräsentiert eine Vergleichsoperation einer Suchanfrage.
 */
public class ComparisonOperation {
    private final String _unit;
    private final Operator _operator;
    private final double _value;

    /**
     * Erzeugt ein Objekt vom Typ ComparisonOperation
     *
     * @param unit     Einheit des Vergleichs (z.B. "baujahr")
     * @param operator Operation des Vergleichs
     * @param value    Zahelnwert des Vergleichs
     */
    public ComparisonOperation(final String unit, final Operator operator, final double value) {
        _unit = unit;
        _operator = operator;
        _value = value;
    }

    /**
     * Liefert die Einheit der Vergleichsoperation als String
     * @return die Einheit der Vergleichsopertaion
     */
    public String getUnit() {
        return _unit;
    }

    /**
     * Liefert den Operator der Vergleichsoperation
     * @return der Operator der Vergleichsoperation
     */
    public Operator getOperator() {
        return _operator;
    }

    /**
     * Liefert den Zahlenwert der Vergleichsoperation
     * @return der Zahlenwert der Vergleichsoperation
     */
    public double getValue() {
        return _value;
    }
}
