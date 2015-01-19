package service;

import domain.Term;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.TermRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class TermServiceImpl implements TermService {

    private final TermRepository _termRepository;

    public TermServiceImpl(TermRepository termRepository) {
        _termRepository = termRepository;
    }

    @Override
    public Collection<String> findTermNamesByIncompleteName(String name) {
        final Collection<String> resultsAsString = new ArrayList<>();
        final Iterable<Term> resultTerms = _termRepository.findByIncompleteName(name);
        for (final Term term : resultTerms) {
            resultsAsString.add(term.getName());
        }
        return resultsAsString;
    }
}
