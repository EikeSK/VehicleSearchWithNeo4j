package repositories;

import domain.Term;
import org.springframework.data.repository.CrudRepository;

public interface TermRepository extends CrudRepository<Term, String>, TermRepositoryCustom {

    public Term findByName(String name);
}
