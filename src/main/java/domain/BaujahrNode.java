package domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class BaujahrNode extends AbstractNeo4jEntity {

    private double value;

    @RelatedTo(type = "MATCHES_FOR", direction = Direction.OUTGOING)
    @Fetch
    private Set<VehicleNode> relatedNodes;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void addRelationTo(final VehicleNode node) {
        if (relatedNodes == null) {
            relatedNodes = new HashSet<>();
        }
        relatedNodes.add(node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaujahrNode that = (BaujahrNode) o;

        return Double.compare(that.value, value) == 0;
    }

    public Set<VehicleNode> getRelatedNodes() {
        return relatedNodes;
    }
}
