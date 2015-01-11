package service;

import domain.NodeMetaData;
import domain.Term;
import domain.VehicleNode;
import org.springframework.stereotype.Component;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import support.StringSplitterUtils;

import java.util.*;

import static com.google.common.base.Ascii.toLowerCase;

@Component
public class VehicleDataPersistenceServiceImpl implements VehicleDataPersistenceService {

    private final VehicleNodeRepository _vehicleNodeRepository;
    private final TermRepository _termRepository;

    public VehicleDataPersistenceServiceImpl(VehicleNodeRepository vehicleNodeRepository, TermRepository termRepository) {
        _vehicleNodeRepository = vehicleNodeRepository;
        _termRepository = termRepository;
    }

    @Override
    public void tokenizeAndSave(final VehicleNode vehicleNode) {
        tokenizeAndSave(vehicleNode, null);
    }

    @Override
    public void tokenizeAndSave(final VehicleNode vehicleNode, final Set<NodeMetaData> additionalMetaData) {
        final Collection<Term> termsFromTokens = getTermsFrom(vehicleNode);
        if (additionalMetaData != null) {
            termsFromTokens.addAll(getTermsFor(vehicleNode, additionalMetaData));
        }
        _vehicleNodeRepository.save(vehicleNode);
        _termRepository.save(termsFromTokens);
    }

    public void tokenizeAndSaveBatch(final Map<VehicleNode, Set<NodeMetaData>> batchData) {
        final List<Term> allTerms = relateAllTermsToNodes(batchData);
        _vehicleNodeRepository.save(batchData.keySet());
        _termRepository.save(allTerms);
    }

    private List<Term> relateAllTermsToNodes(Map<VehicleNode, Set<NodeMetaData>> batchData) {
        final List<Term> allTerms = new ArrayList<>();
        for (final VehicleNode node : batchData.keySet()) {
            Collection<Term> termsForNode = getTermsFrom(node);
            termsForNode.addAll(getTermsFor(node, batchData.get(node)));
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

    private Collection<Term> getTermsFor(final VehicleNode vehicleNode, final Set<NodeMetaData> tokens) {
        final Collection<Term> terms = new ArrayList<>();
        for (NodeMetaData token : tokens) {
            Term term = _termRepository.findByName(token.getName());
            if (term == null) {
                term = new Term();
            }
            term.setName(toLowerCase(token.getName()));
            if (token.getUnit() != null) {
                term.setUnit(toLowerCase(token.getUnit()));
            }
            term.addRelationTo(vehicleNode);
            terms.add(term);
        }
        return terms;
    }

    private Collection<Term> getTermsFrom(final VehicleNode vehicleNode) {
        final Set<String> tokenizedModelName = StringSplitterUtils.tokenize(vehicleNode);
        final Set<NodeMetaData> metaDataForNode = new HashSet<>();
        for (String token : tokenizedModelName) {
            final NodeMetaData metaData = new NodeMetaData();
            metaData.setName(token);
            metaDataForNode.add(metaData);
        }
        return getTermsFor(vehicleNode, metaDataForNode);
    }

}
