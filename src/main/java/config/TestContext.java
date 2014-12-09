package config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.support.Neo4jTemplate;

@Configuration
@EnableNeo4jRepositories(basePackages = "repositories")
public class TestContext extends Neo4jConfiguration {

    public TestContext() {
        setBasePackage("domain", "repositories");
    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new TestGraphDatabaseFactory().newImpermanentDatabase();
        //return new SpringRestGraphDatabase("http://localhost:7474/db/data");
    }

    @Bean
    public Neo4jTemplate neo4jTemplate() {
        return new Neo4jTemplate(graphDatabaseService());
    }

}
