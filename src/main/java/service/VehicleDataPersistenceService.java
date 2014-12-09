package service;

import com.google.common.base.Splitter;
import domain.Term;
import domain.VehicleModel;
import repositories.TermRepository;
import repositories.VehicleModelRepository;
import support.StringSplitterUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class VehicleDataPersistenceService {

    private final VehicleModelRepository _vehicleModelRepository;
    private final TermRepository _termRepository;

    public VehicleDataPersistenceService(VehicleModelRepository vehicleModelRepository, TermRepository termRepository) {
        _vehicleModelRepository = vehicleModelRepository;
        _termRepository = termRepository;
    }

    public void tokenizeAndSave(final VehicleModel vehicleModel) {
        final Collection<Term> termsFromTokens = getTermsFrom(vehicleModel);
        _vehicleModelRepository.save(vehicleModel);
        _termRepository.save(termsFromTokens);
    }

    private Collection<Term> getTermsFrom(final VehicleModel vehicleModel) {
        final Set<String> tokenziedModelName = StringSplitterUtils.tokenize(vehicleModel);
        final Collection<Term> terms = new ArrayList<>();
        for (String token : tokenziedModelName) {
            Term term = _termRepository.findByName(token);
            if (term == null) {
                term = new Term();
            }
            term.setName(token);
            term.addRelationTo(vehicleModel);
            terms.add(term);
        }
        return terms;
    }

}
