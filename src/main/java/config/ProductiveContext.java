package config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import repositories.TermRepository;
import repositories.VehicleModelRepository;
import service.VehicleDataPersistenceService;

@Configuration
@EnableNeo4jRepositories(basePackages = "repositories")
public class ProductiveContext extends Neo4jConfiguration {

    public ProductiveContext() {
        setBasePackage("domain", "repositories");
    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase("http://localhost:7474/db/data");
    }

    @Bean
    public Neo4jTemplate neo4jTemplate() {
        return new Neo4jTemplate(graphDatabaseService());
    }

    @Bean
    public VehicleDataPersistenceService vehicleDataPersistenceService() {
        return new VehicleDataPersistenceService(vehicleModelRepository, termRepository);
    }

    @Autowired
    public VehicleModelRepository vehicleModelRepository;

    @Autowired
    public TermRepository termRepository;

}
