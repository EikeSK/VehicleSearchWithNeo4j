package service;

import com.google.common.base.Splitter;
import domain.Term;
import domain.VehicleModel;
import repositories.TermRepository;
import repositories.VehicleModelRepository;

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
        final Set<String> tokenziedModelName = tokenize(vehicleModel);
        final Collection<Term> terms = new ArrayList<>();
        for (String token : tokenziedModelName) {
            final Term term = new Term();
            term.setName(token);
            term.addRelationTo(vehicleModel);   //TODO: Relation an vorhandenen Token setzten, wenn verfügbar, statt zu überschreiben
            terms.add(term);
        }
        return terms;
    }

    private Set<String> tokenize(final VehicleModel vehicleModel) {
        final Set<String> tokens = new HashSet<>();
        final Splitter splitter = Splitter.on(" ").omitEmptyStrings().trimResults();
        if (vehicleModel != null) {
            final Iterable<String> tokenIterable = splitter.split(vehicleModel.getName());
            for (String token : tokenIterable) {
                tokens.add(token);  // TODO: toLowerCase für einheitliche Terms in der Datenbank?
            }
        }
        return tokens;
    }
}
