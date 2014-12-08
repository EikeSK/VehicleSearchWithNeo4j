package domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Term {

    @GraphId
    private Long _id;

    @Indexed(indexName = "terms", indexType = IndexType.FULLTEXT, unique = true)
    private String name;

    @RelatedTo(type = "MATCHES_FOR", direction = Direction.OUTGOING)
    private
    @Fetch
    Set<VehicleModel> relatedModels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRelationTo(final VehicleModel model) {
        if (relatedModels == null) {
            relatedModels = new HashSet<>();
        }
        relatedModels.add(model);
    }

    public Set<VehicleModel> getRelatedModels() {
        return relatedModels;
    }
}
