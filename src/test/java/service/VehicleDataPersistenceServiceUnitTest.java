package service;

import domain.Term;
import domain.VehicleModel;
import org.aspectj.weaver.PerTypeWithinTargetTypeMunger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import repositories.TermRepository;
import repositories.VehicleModelRepository;

import java.util.Collection;
import java.util.Set;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class VehicleDataPersistenceServiceUnitTest {

    private VehicleDataPersistenceService _vehicleDataPersistenceService;
    private VehicleModelRepository _vehicleModelRepository;
    private TermRepository _termRepository;

    @Before
    public void setUp() throws Exception {
        _termRepository = mock(TermRepository.class);
        _vehicleModelRepository = mock(VehicleModelRepository.class);
        _vehicleDataPersistenceService = new VehicleDataPersistenceService(_vehicleModelRepository, _termRepository);
    }

    @Test
    public void shouldInvokeSaveOnVehicleModelRepository() throws Exception {
        final VehicleModel vehicleModel = vehicleModelWithName("Test Entity");

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModel);

        verify(_vehicleModelRepository).save(vehicleModel);
        verifyNoMoreInteractions(_vehicleModelRepository);
    }

    @Test
    public void shouldInvokeSaveOnTermRepositoryForTermInName() throws Exception {
        final VehicleModel vehicleModel = vehicleModelWithName("Testmodel");

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModel);

        verify(_termRepository).save(argThat(new ArgumentMatcher<Collection<Term>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Term> result = (Collection<Term>) o;
                final Term resultTerm = result.iterator().next();
                return resultTerm.getName().equals("Testmodel") && resultTerm.getRelatedModels().contains(vehicleModel);
            }
        }));
    }

    @Test
    public void shouldInvokeSaveOnTermRepositoryForTermsInNameWithMultipleEntries() {
        final VehicleModel vehicleModel = vehicleModelWithName("Test Entity");

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModel);

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
        final VehicleModel vehicleModel = vehicleModelWithName("Test Entity");
        final Term term = new Term();
        term.setName("Test");
        term.addRelationTo(vehicleModelWithName("Test"));

        when(_termRepository.findByName("Test")).thenReturn(term);

        _vehicleDataPersistenceService.tokenizeAndSave(vehicleModel);

        verify(_termRepository).save(argThat(new ArgumentMatcher<Collection<Term>>() {
            @Override
            public boolean matches(Object o) {
                @SuppressWarnings("unchecked") final Collection<Term> result = (Collection<Term>) o;
                final Term resultTerm = result.iterator().next();
                final Set<VehicleModel> relatedModels = resultTerm.getRelatedModels();
                return relatedModels.size() == 2 && relatedModels.contains(vehicleModel);
            }
        }));
    }

    private VehicleModel vehicleModelWithName(final String name) {
        final VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setName(name);
        return vehicleModel;
    }
}