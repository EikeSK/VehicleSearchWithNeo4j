package service;

import domain.BaujahrNode;
import domain.Term;
import domain.VehicleNode;
import org.springframework.stereotype.Component;
import repositories.BaujahrNodeRepository;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import support.StringSplitterUtils;
import support.VehicleMetaData;

import java.util.*;

import static com.google.common.base.Ascii.toLowerCase;

@Component
public class VehicleDataPersistenceServiceImpl implements VehicleDataPersistenceService {

    private final VehicleNodeRepository _vehicleNodeRepository;
    private final TermRepository _termRepository;
    private final BaujahrNodeRepository _baujahrNodeRepository;

    public VehicleDataPersistenceServiceImpl(VehicleNodeRepository vehicleNodeRepository, TermRepository termRepository, BaujahrNodeRepository baujahrNodeRepository) {
        _vehicleNodeRepository = vehicleNodeRepository;
        _termRepository = termRepository;
        _baujahrNodeRepository = baujahrNodeRepository;
    }

    @Override
    public void tokenizeAndSave(final VehicleNode vehicleNode) {
        tokenizeAndSave(vehicleNode, null);
    }

    @Override
    public void tokenizeAndSave(final VehicleNode vehicleNode, final VehicleMetaData additionalMetaData) {
        final Collection<Term> termsFromTokens = getTermsFrom(vehicleNode);
        if (additionalMetaData != null) {
            termsFromTokens.addAll(getTermsFor(vehicleNode, additionalMetaData.getAdditionalMetaData()));
        }
        _vehicleNodeRepository.save(vehicleNode);
        _termRepository.save(termsFromTokens);
        if (additionalMetaData != null && additionalMetaData.getBaujahr() > 0) {
            _baujahrNodeRepository.save(getBaujahrFrom(vehicleNode, additionalMetaData));
        }
    }

    public void tokenizeAndSaveBatch(final Map<VehicleNode, VehicleMetaData> batchData) {
        final List<Term> allTerms = relateAllTermsToNodes(batchData);
        _vehicleNodeRepository.save(batchData.keySet());
        _termRepository.save(allTerms);
    }

    private List<Term> relateAllTermsToNodes(Map<VehicleNode, VehicleMetaData> batchData) {
        final List<Term> allTerms = new ArrayList<>();
        for (final VehicleNode node : batchData.keySet()) {
            Collection<Term> termsForNode = getTermsFrom(node);
            termsForNode.addAll(getTermsFor(node, batchData.get(node).getAdditionalMetaData()));
            for (final Term term : termsForNode) {
                if (allTerms.contains(term)) {
                    int index = allTerms.indexOf(term);
                    final Term foundTerm = allTerms.get(index);
                    foundTerm.addRelationTo(node);
                    allTerms.set(index, foundTerm);
                } else {
                    allTerms.add(term);
                }
            }
        }
        return allTerms;
    }

    private BaujahrNode getBaujahrFrom(final VehicleNode vehicleNode, final VehicleMetaData additionalMetaData) {
        BaujahrNode baujahrNode = _baujahrNodeRepository.findByValue(additionalMetaData.getBaujahr());
        if (baujahrNode == null) {
            baujahrNode = new BaujahrNode();
            baujahrNode.setValue(additionalMetaData.getBaujahr());
        }
        baujahrNode.addRelationTo(vehicleNode);
        return baujahrNode;
    }

    private Collection<Term> getTermsFor(final VehicleNode vehicleNode, final Set<String> tokens) {
        final Collection<Term> terms = new ArrayList<>();
        for (final String token : tokens) {
            Term term = _termRepository.findByName(token);
            if (term == null) {
                term = new Term();
            }
            term.setName(toLowerCase(token));
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
