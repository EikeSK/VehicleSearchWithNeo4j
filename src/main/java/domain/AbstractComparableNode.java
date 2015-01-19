package domain;

public abstract class AbstractComparableNode extends AbstractNeo4jEntity {

    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
