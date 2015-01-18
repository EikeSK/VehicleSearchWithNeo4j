package service;

import domain.Baujahr;
import domain.Term;
import domain.VehicleNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import repositories.BaujahrNodeRepository;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import support.VehicleMetaData;

import java.util.*;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class VehicleDataPersistenceServiceImplUnitTest {

    private VehicleDataPersistenceServiceImpl _vehicleDataPersistenceService;
    private VehicleNodeRepository _vehicleNodeRepository;
    private TermRepository _termRepository;
    private BaujahrNodeRepository _baujahrNodeRepository;

    @Before
    public void setUp() throws Exception {
        _termRepository = mock(TermRepository.class);
        _vehicleNodeRepository = mock(VehicleNodeRepository.class);
        _baujahrNodeRepository = mock(BaujahrNodeRepository.class);
        _vehicleDataPersistenceService = new VehicleDataPersistenceServiceImpl(_vehicleNodeRepository, _termRepository, _baujahrNodeRepository);
    }

    @Test
    public void shouldInvokeSaveOnVehicleNodeRepository() throws Exception {
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
                return resultTerm.getName().equals("testmodel") && resultTerm.getRelatedNodes().contains(vehicleNode);
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
                final Set<VehicleNode> relatedNodes = resultTerm.getRelatedNodes();
                return relatedNodes.size() == 2;
            }
        }));
    }

    @Test
    public void shouldSaveAdditionalMetaData() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test Model");
        final VehicleMetaData additionalMetaData = vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("benzin")));

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode, additionalMetaData);

        verify(_termRepository).save(termsWithSize(3));
    }

    @Test
    public void shouldSaveBaujahr() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test");
        final VehicleMetaData vehicleMetaData = new VehicleMetaData();
        vehicleMetaData.setBaujahrFrom(2006);

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode, vehicleMetaData);

        verify(_baujahrNodeRepository).save(argThat(new ArgumentMatcher<Baujahr>() {
            @Override
            public boolean matches(Object o) {
                final Baujahr baujahr = (Baujahr) o;
                return baujahr.getValue() == 2006.0;
            }
        }));
    }

    @Test
    public void shouldSaveBaujahrRange() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test");
        final VehicleMetaData vehicleMetaData = new VehicleMetaData();
        vehicleMetaData.setBaujahrFrom(2006);
        vehicleMetaData.setBaujahrTo(2010);

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode, vehicleMetaData);

        verify(_baujahrNodeRepository, times(5)).save(any(Baujahr.class));
    }

    @Test
    public void shouldNotSaveBaujahrIfMetaDataDoesNotContainBaujahr() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test");
        final VehicleMetaData vehicleMetaData = new VehicleMetaData();
        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode, vehicleMetaData);

        verifyZeroInteractions(_baujahrNodeRepository);
    }

    @Test
    public void shouldUpdateRelationInBaujahrIfAlreadyExistsForOtherNode() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test");
        final VehicleMetaData vehicleMetaData = new VehicleMetaData();
        vehicleMetaData.setBaujahrFrom(2006);

        final Baujahr existingBaujahr = new Baujahr();
        existingBaujahr.addRelationTo(vehicleNodeWithName("Test2"));

        when(_baujahrNodeRepository.findByValue(2006)).thenReturn(existingBaujahr);

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode, vehicleMetaData);

        verify(_baujahrNodeRepository).save(argThat(new ArgumentMatcher<Baujahr>() {
            @Override
            public boolean matches(Object o) {
                final Baujahr baujahr = (Baujahr) o;
                return baujahr.getRelatedNodes().size() == 2;
            }
        }));

    }

    @Test
    public void batchShouldSaveNodesAndTerms() throws Exception {
        final Map<VehicleNode, VehicleMetaData> batchData = new HashMap<>();
        batchData.put(vehicleNodeWithName("TestNode1"), vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("test1", "test2"))));
        batchData.put(vehicleNodeWithName("TestNode2"), vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("test3", "test4"))));

        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batchData);

        verify(_vehicleNodeRepository).save(vehicleNodesWithSize(2));

        verify(_termRepository).save(termsWithSize(6));
    }

    @Test
    public void batchShouldSaveNodesAndTermsAndBaujahrNodes() throws Exception {
        final Map<VehicleNode, VehicleMetaData> batchData = new HashMap<>();
        batchData.put(vehicleNodeWithName("TestNode1"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("test1", "test2")), 2006));
        batchData.put(vehicleNodeWithName("TestNode2"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("test3", "test4")), 2007));

        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batchData);

        verify(_vehicleNodeRepository).save(vehicleNodesWithSize(2));
        verify(_termRepository).save(termsWithSize(6));
        verify(_baujahrNodeRepository).save(baujahrNodesWithSize(2));
    }

    @Test
    public void batchShouldSaveBaujahrNodesWithoutDoublicates() throws Exception {
        final Map<VehicleNode, VehicleMetaData> batchData = new HashMap<>();
        batchData.put(vehicleNodeWithName("TestNode1"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("test1")), 2006));
        batchData.put(vehicleNodeWithName("TestNode2"), vehicleMetaDataWithTermsAndBaujahr(new HashSet<>(Arrays.asList("test3")), 2006));

        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batchData);

        verify(_baujahrNodeRepository).save(argThat(new ArgumentMatcher<Iterable<Baujahr>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Iterable<Baujahr> baujahrNodes = (Iterable<Baujahr>) o;
                return baujahrNodes.iterator().hasNext() && baujahrNodes.iterator().next().getRelatedNodes().size() == 2;
            }
        }));
    }


    @Test
    public void batchShouldCheckIfBaujahrAlreadyExist() throws Exception {
        final Map<VehicleNode, VehicleMetaData> batchData = new HashMap<>();
        batchData.put(vehicleNodeWithName("TestNode1"), vehicleMetaDataWithTermsAndBaujahr(Collections.<String>emptySet(), 2006));

        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batchData);

        verify(_baujahrNodeRepository).findByValue(2006);
    }


    @Test
    public void batchShouldCheckIfTermAlreadyExist() throws Exception {
        final Map<VehicleNode, VehicleMetaData> batchData = new HashMap<>();
        batchData.put(vehicleNodeWithName("TestNode1"), vehicleMetaDataWithTerms(new HashSet<>(Arrays.asList("test1", "test2"))));

        _vehicleDataPersistenceService.tokenizeAndSaveBatch(batchData);

        verify(_termRepository, times(3)).findByName(anyString());
    }


    @Test
    public void shouldUpdateRelationInBatchIfTermAlreadyExists() throws Exception {
        final VehicleNode vehicleNode = vehicleNodeWithName("Test");
        final Map<VehicleNode, VehicleMetaData> batchData = new HashMap<>();
        batchData.put(vehicleNode, vehicleMetaDataWithTerms(Collections.<String>emptySet()));
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
                final Set<VehicleNode> relatedNodes = resultTerm.getRelatedNodes();
                return relatedNodes.size() == 2;
            }
        }));
    }

    private VehicleNode vehicleNodeWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }

    private VehicleMetaData vehicleMetaDataWithTerms(final Set<String> additionalTerms) {
        return vehicleMetaDataWithTermsAndBaujahr(additionalTerms, 0);
    }

    private VehicleMetaData vehicleMetaDataWithTermsAndBaujahr(final Set<String> additionalTerms, final int baujahr) {
        final VehicleMetaData vehicleMetaData = new VehicleMetaData();
        vehicleMetaData.setAdditionalMetaData(additionalTerms);
        vehicleMetaData.setBaujahrFrom(baujahr);
        return vehicleMetaData;
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

    private Collection<Baujahr> baujahrNodesWithSize(int size) {
        return argThat(new ArgumentMatcher<Collection<Baujahr>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Baujahr> result = (Collection<Baujahr>) o;
                return result.size() == size;
            }
        });
    }

}