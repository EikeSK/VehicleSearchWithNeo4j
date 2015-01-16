package repositories;

import domain.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import support.VehicleSearchQueryGenerator;

@Service
public class TermRepositoryImpl implements TermRepositoryCustom {

    @Autowired
    private Neo4jTemplate _neo4jTemplate;

    public Iterable<Term> findByIncompleteName(String name) {
        final String cypherQuery = VehicleSearchQueryGenerator.generateCypherQueryForAutocompletion(name);
        return _neo4jTemplate.query(cypherQuery, null).to(Term.class);
    }
}
