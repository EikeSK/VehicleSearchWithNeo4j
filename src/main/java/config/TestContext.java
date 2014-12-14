package config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import search.SearchEngine;
import service.VehicleDataPersistenceService;
import support.VehicleSearchQueryGenerator;

@Configuration
@EnableNeo4jRepositories(basePackages = "repositories")
public class TestContext extends Neo4jConfiguration {

    public TestContext() {
        setBasePackage("domain", "repositories");
    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new TestGraphDatabaseFactory().newImpermanentDatabase();
    }

    @Bean
    public Neo4jTemplate neo4jTemplate() {
        return new Neo4jTemplate(graphDatabaseService());
    }

    @Bean
    public VehicleDataPersistenceService vehicleDataPersistenceService() {
        return new VehicleDataPersistenceService(_vehicleNodeRepository, termRepository);
    }

    @Bean
    public VehicleSearchQueryGenerator vehicleSearchQueryGenerator() {
        return new VehicleSearchQueryGenerator();
    }

    @Bean
    public SearchEngine searchEngine() {
        return new SearchEngine(vehicleSearchQueryGenerator());
    }

    @Autowired
    public VehicleNodeRepository _vehicleNodeRepository;

    @Autowired
    public TermRepository termRepository;
}
