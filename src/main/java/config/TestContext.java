package config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import repositories.BaujahrNodeRepository;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import search.SearchEngine;
import search.SearchEngineImpl;
import service.*;

@Configuration
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = "repositories")
public class TestContext extends Neo4jConfiguration {

    public TestContext() {
        setBasePackage("domain", "repositories");
    }

    @Autowired
    public VehicleNodeRepository _vehicleNodeRepository;

    @Autowired
    public TermRepository _termRepository;

    @Autowired
    public BaujahrNodeRepository _baujahrNodeRepository;

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new TestGraphDatabaseFactory().newImpermanentDatabase();
    }

    @Bean
    public Neo4jTemplate neo4jTemplate() {
        return new Neo4jTemplate(graphDatabaseService());
    }

    @Bean
    public VehicleDataPersistenceServiceImpl vehicleDataPersistenceService() {
        return new VehicleDataPersistenceServiceImpl(_vehicleNodeRepository, _termRepository, _baujahrNodeRepository);
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
