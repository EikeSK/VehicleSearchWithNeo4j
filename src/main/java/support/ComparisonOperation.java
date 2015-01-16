package support;


public class ComparisonOperation {
    private String _unit;
    private Operator _operator;
    private double _value;

    public ComparisonOperation(final String unit, final Operator operator, final double value) {
        _unit = unit;
        _operator = operator;
        _value = value;
    }

    public String getUnit() {
        return _unit;
    }

    public Operator getOperator() {
        return _operator;
    }

    public double getValue() {
        return _value;
    }
}
