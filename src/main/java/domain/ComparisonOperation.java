package domain;


import service.Operator;

public class ComparisonOperation {
    private Operator _operator;
    private NodeMetaData _nodeMetaData;

    public ComparisonOperation(Operator operator, NodeMetaData nodeMetaData) {
        _operator = operator;
        _nodeMetaData = nodeMetaData;
    }

    public Operator getOperator() {
        return _operator;
    }

    public NodeMetaData getNodeMetaData() {
        return _nodeMetaData;
    }
}
