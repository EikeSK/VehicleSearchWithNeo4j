package service;

import domain.NodeMetaData;
import domain.Term;
import domain.VehicleNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;

import java.util.*;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static support.TestUtils.metaDataWith;

public class VehicleDataPersistenceServiceImplUnitTest {

    private VehicleDataPersistenceServiceImpl _vehicleDataPersistenceService;
    private VehicleNodeRepository _vehicleNodeRepository;
    private TermRepository _termRepository;

    @Before
    public void setUp() throws Exception {
        _termRepository = mock(TermRepository.class);
        _vehicleNodeRepository = mock(VehicleNodeRepository.class);
        _vehicleDataPersistenceService = new VehicleDataPersistenceServiceImpl(_vehicleNodeRepository, _termRepository);
    }

    @Test
    public void shouldInvokeSaveOnVehicleModelRepository() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test Entity");

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode);

        verify(_vehicleNodeRepository).save(vehicleNode);
        verifyNoMoreInteractions(_vehicleNodeRepository);
    }

    @Test
    public void shouldInvokeSaveOnTermRepositoryForTermInName() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Testmodel");

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode);

        verify(_termRepository).save(argThat(new ArgumentMatcher<Collection<Term>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Term> result = (Collection<Term>) o;
                final Term resultTerm = result.iterator().next();
                return resultTerm.getName().equals("testmodel") && resultTerm.getRelatedModels().contains(vehicleNode);
            }
        }));
    }

    @Test
    public void shouldInvokeSaveOnTermRepositoryForTermsInNameWithMultipleEntries() {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test Entity");

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode);

        verify(_termRepository).save(argThat(new ArgumentMatcher<Collection<Term>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Term> result = (Collection<Term>) o;
                return result.size() == 2;
            }
        }));
    }

    @Test
    public void shouldUpdateRelationIfTermAlreadyExists() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test");
        final Term term = new Term();
        term.setName("Test");
        term.addRelationTo(vehicleNodeWithName("Test"));

        when(_termRepository.findByName("test")).thenReturn(term);

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode);

        verify(_termRepository).save(argThat(new ArgumentMatcher<Collection<Term>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Term> result = (Collection<Term>) o;
                final Term resultTerm = result.iterator().next();
                final Set<VehicleNode> relatedModels = resultTerm.getRelatedModels();
                return relatedModels.size() == 2;
            }
        }));
    }

    @Test
    public void shouldSaveAdditionalMetaData() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test Model");
        final Set<NodeMetaData> additionalMetaData = new HashSet<>(Arrays.asList(metaDataWith("benzin")));

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode, additionalMetaData);

        verify(_termRepository).save(termsWithSize(3));
    }

    @Test
    public void batchShouldSaveNodesAndTerms() throws Exception {
        final Map<VehicleNode, Set<NodeMetaData>> batchData = new HashMap<>();
        batchData.put(vehicleNodeWithName("TestNode1"), new HashSet<>(Arrays.asList(metaDataWith("test1"), metaDataWith("test2"))));
        batchData.put(vehicleNodeWithName("TestNode2"), new HashSet<>(Arrays.asList(metaDataWith("test3"), metaDataWith("test4"))));

        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batchData);

        verify(_vehicleNodeRepository).save(vehicleNodesWithSize(2));

        verify(_termRepository).save(termsWithSize(6));
    }

    @Test
    public void batchShouldCheckIfTermAlreadyExist() throws Exception {
        final Map<VehicleNode, Set<NodeMetaData>> batchData = new HashMap<>();
        batchData.put(vehicleNodeWithName("TestNode1"), new HashSet<>(Arrays.asList(metaDataWith("test1"), metaDataWith("test2"))));

        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batchData);

        verify(_termRepository, times(3)).findByName(anyString());
    }

    @Test
    public void shouldUpdateRelationInBatchIfTermAlreadyExists() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test");
        final Map<VehicleNode, Set<NodeMetaData>> batchData = new HashMap<>();
        batchData.put(vehicleNode, Collections.<NodeMetaData>emptySet());
        final Term term = new Term();
        term.setName("Test");
        term.addRelationTo(vehicleNodeWithName("Test"));

        when(_termRepository.findByName("test")).thenReturn(term);

        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batchData);

        verify(_termRepository).save(argThat(new ArgumentMatcher<Collection<Term>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Term> result = (Collection<Term>) o;
                final Term resultTerm = result.iterator().next();
                final Set<VehicleNode> relatedModels = resultTerm.getRelatedModels();
                return relatedModels.size() == 2;
            }
        }));
    }

    private VehicleNode vehicleNodeWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }


    private Collection<VehicleNode> vehicleNodesWithSize(final int size) {
        return argThat(new ArgumentMatcher<Collection<VehicleNode>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<VehicleNode> result = (Collection<VehicleNode>) o;
                return result.size() == size;
            }
        });
    }

    private Collection<Term> termsWithSize(final int size) {
        return argThat(new ArgumentMatcher<Collection<Term>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Term> result = (Collection<Term>) o;
                return result.size() == size;
            }
        });
    }

}