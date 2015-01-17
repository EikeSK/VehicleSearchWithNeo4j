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

    // TODO: getter für Value von Interface implentieren, als sogenannte "ValueNodes"

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

    public Set<VehicleNode> getRelatedNodes() {
        return relatedNodes;
    }
}
