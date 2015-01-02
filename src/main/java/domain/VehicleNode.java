package domain;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class VehicleNode extends AbstractNeo4jEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
