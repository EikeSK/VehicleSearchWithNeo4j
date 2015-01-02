package domain;

import org.springframework.data.neo4j.annotation.GraphId;

public abstract class AbstractNeo4jEntity {
    @GraphId
    private Long _id;
}
