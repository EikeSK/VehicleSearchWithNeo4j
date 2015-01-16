package repositories;

import domain.Term;

public interface TermRepositoryCustom {

    public Iterable<Term> findByIncompleteName(final String name);

}
