package service;

public enum Operator {
    GREATER(">"),
    SMALLER("<"),
    GREATER_EQUALS(">="),
    SMALLER_EQUALS("<=");

    private String _operation;

    Operator(String operation) {
        _operation = operation;
    }

    public String getOperation() {
        return _operation;
    }

    public static Operator findByOperation(final String operation) {
        for (Operator operator : Operator.values()) {
            if (operator.getOperation().equals(operation)) {
                return operator;
            }
        }
        return null;
    }
}
