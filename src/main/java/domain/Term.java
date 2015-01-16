package domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Term extends AbstractNeo4jEntity {

    @Indexed(indexName = "terms", indexType = IndexType.FULLTEXT, unique = true)
    private String name;

    @RelatedTo(type = "MATCHES_FOR", direction = Direction.OUTGOING)
    @Fetch
    private Set<VehicleNode> relatedNodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRelationTo(final VehicleNode node) {
        if (relatedNodes == null) {
            relatedNodes = new HashSet<>();
        }
        relatedNodes.add(node);
    }

    @Override
    public boolean equals(Object o) {
        Term term = (Term) o;
        return name.equals(term.getName());
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public Set<VehicleNode> getRelatedNodes() {
        return relatedNodes;
    }
}
