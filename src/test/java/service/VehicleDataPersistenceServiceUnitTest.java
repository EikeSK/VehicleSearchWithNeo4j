package service;

import domain.Term;
import domain.VehicleNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class VehicleDataPersistenceServiceUnitTest {

    private VehicleDataPersistenceService _vehicleDataPersistenceService;
    private VehicleNodeRepository _vehicleNodeRepository;
    private TermRepository _termRepository;

    @Before
    public void setUp() throws Exception {
        _termRepository = mock(TermRepository.class);
        _vehicleNodeRepository = mock(VehicleNodeRepository.class);
        _vehicleDataPersistenceService = new VehicleDataPersistenceService(_vehicleNodeRepository, _termRepository);
    }

    @Test
    public void shouldInvokeSaveOnVehicleModelRepository() throws Exception {
        final VehicleNode vehicleNode = vehicleModelWithName("Test Entity");

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode);

        verify(_vehicleNodeRepository).save(vehicleNode);
        verifyNoMoreInteractions(_vehicleNodeRepository);
    }

    @Test
    public void shouldInvokeSaveOnTermRepositoryForTermInName() throws Exception {
        final VehicleNode vehicleNode = vehicleModelWithName("Testmodel");

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode);

        verify(_termRepository).save(argThat(new ArgumentMatcher<Collection<Term>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Term> result = (Collection<Term>) o;
                final Term resultTerm = result.iterator().next();
                return resultTerm.getName().equals("Testmodel") && resultTerm.getRelatedModels().contains(vehicleNode);
            }
        }));
    }

    @Test
    public void shouldInvokeSaveOnTermRepositoryForTermsInNameWithMultipleEntries() {
        final VehicleNode vehicleNode = vehicleModelWithName("Test Entity");

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
        final VehicleNode vehicleNode = vehicleModelWithName("Test");
        final Term term = new Term();
        term.setName("Test");
        term.addRelationTo(vehicleModelWithName("Test"));

        when(_termRepository.findByName("Test")).thenReturn(term);

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
        final VehicleNode vehicleNode = vehicleModelWithName("Test Model");
        final Set<String> additionalMetaData = new HashSet<>(Arrays.asList("benzin"));

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleNode, additionalMetaData);

        verify(_termRepository).save(argThat(new ArgumentMatcher<Collection<Term>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Term> result = (Collection<Term>) o;
                return result.size() == 3;
            }
        }));
    }

    private VehicleNode vehicleModelWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}