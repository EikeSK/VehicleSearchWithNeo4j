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
    private Set<VehicleNode> relatedModels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRelationTo(final VehicleNode model) {
        if (relatedModels == null) {
            relatedModels = new HashSet<>();
        }
        relatedModels.add(model);
    }

    public Set<VehicleNode> getRelatedModels() {
        return relatedModels;
    }
}
