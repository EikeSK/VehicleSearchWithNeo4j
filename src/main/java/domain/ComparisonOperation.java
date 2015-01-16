package domain;


import support.Operator;

public class ComparisonOperation {
    private Operator _operator;
    private String _unit;
    private double _value;

    public ComparisonOperation(Operator operator, String unit, double value) {
        _operator = operator;
        _unit = unit;
        _value = value;
    }

    public Operator getOperator() {
        return _operator;
    }

    public String getUnit() {
        return _unit;
    }

    public double getValue() {
        return _value;
    }
}
