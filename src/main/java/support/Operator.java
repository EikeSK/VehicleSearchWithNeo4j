package support;

/**
 * Repräsentiert alle möglichen Vergleichsoperationen für eine Suchanfrage.
 * Die Operatoren lassen sich dabei auch in der Abfragesprache Cypher abbilden.
 */
public enum Operator {
    GREATER(">"),
    SMALLER("<"),
    GREATER_EQUALS(">="),
    SMALLER_EQUALS("<="),
    EQUALS("=");

    private final String _operation;

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
