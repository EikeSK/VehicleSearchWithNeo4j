package repositories;

import domain.Term;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import support.TestContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
public class TermRepositoryIntegrationTest {

    @Autowired
    private TermRepository _termRepository;

    @Autowired
    @Qualifier("graphDatabaseService")
    private GraphDatabaseService _graphDatabaseService;

    @Test
    public void testCRUD() throws Exception {
        final Term term = new Term();
        term.setName("Audi");

        _termRepository.save(term);

        final Term resultTerm = _termRepository.findByName("Audi");

        assertThat(resultTerm, notNullValue());
        assertThat(resultTerm.getName(), equalTo("Audi"));
    }

    @Test
    public void testUniquenessOfTermName() {
        final Term term = new Term();
        term.setName("Audi");
        final Term otherTerm = new Term();
        otherTerm.setName("Audi");

        _termRepository.save(term);
        _termRepository.save(otherTerm);

        List<Term> resultTerm = new ArrayList<>();
        try (Transaction tx = _graphDatabaseService.beginTx()) {
            resultTerm = IteratorUtil.asList(_termRepository.findAll());
            tx.success();
        }

        assertThat(resultTerm, hasSize(1));
        assertThat(resultTerm.get(0).getName(), equalTo("Audi"));
    }

}