package config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import repositories.*;
import search.SearchEngine;
import search.SearchEngineImpl;
import service.VehicleDataPersistenceServiceImpl;

@Configuration
@EnableNeo4jRepositories(basePackages = "repositories")
@EnableTransactionManagement
public class ProductiveContext extends Neo4jConfiguration {

    public ProductiveContext() {
        setBasePackage("domain", "repositories");
    }

    @Autowired
    public VehicleNodeRepository _vehicleNodeRepository;

    @Autowired
    public TermRepository _termRepository;

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase("http://localhost:7474/db/data");
    }

    @Bean
    public Neo4jTemplate neo4jTemplate() {
        return new Neo4jTemplate(graphDatabaseService());
    }

    @Bean
    public VehicleDataPersistenceServiceImpl vehicleDataPersistenceService() {
        return new VehicleDataPersistenceServiceImpl(_vehicleNodeRepository, _termRepository);
    }

    @Bean
    public VehicleNodeService vehicleNodeService() {
        return new VehicleNodeServiceImpl(_vehicleNodeRepository);
    }

    @Bean
    public TermService termService() {
        return new TermServiceImpl(_termRepository);
    }

    @Bean
    public SearchEngine searchEngine() {
        return new SearchEngineImpl(vehicleNodeService(), termService());
    }
}
