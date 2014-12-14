package service;

import domain.Term;
import domain.VehicleNode;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import support.StringSplitterUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class VehicleDataPersistenceService {

    private final VehicleNodeRepository _vehicleNodeRepository;
    private final TermRepository _termRepository;

    public VehicleDataPersistenceService(VehicleNodeRepository vehicleNodeRepository, TermRepository termRepository) {
        _vehicleNodeRepository = vehicleNodeRepository;
        _termRepository = termRepository;
    }

    public void tokenizeAndSave(final VehicleNode vehicleNode) {
        tokenizeAndSave(vehicleNode, null);
    }

    public void tokenizeAndSave(final VehicleNode vehicleNode, final Set<String> additionalMetaData) {
        final Collection<Term> termsFromTokens = getTermsFrom(vehicleNode);
        if (additionalMetaData != null) {
            termsFromTokens.addAll(getTermsFor(vehicleNode, additionalMetaData));
        }
        _vehicleNodeRepository.save(vehicleNode);
        _termRepository.save(termsFromTokens);
    }

    private Collection<Term> getTermsFor(final VehicleNode vehicleNode, final Set<String> tokens) {
        final Collection<Term> terms = new ArrayList<>();
        for (String token : tokens) {
            Term term = _termRepository.findByName(token);
            if (term == null) {
                term = new Term();
            }
            term.setName(token);
            term.addRelationTo(vehicleNode);
            terms.add(term);
        }
        return terms;
    }

    private Collection<Term> getTermsFrom(final VehicleNode vehicleNode) {
        final Set<String> tokenizedModelName = StringSplitterUtils.tokenize(vehicleNode);
        return getTermsFor(vehicleNode, tokenizedModelName);
    }

}
