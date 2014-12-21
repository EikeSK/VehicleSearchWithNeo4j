package search;

import com.google.common.base.Stopwatch;
import config.ProductiveContext;
import domain.VehicleNode;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import service.VehicleDataPersistenceService;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductiveContext.class})
public class SearchEnginePerformanceTest {

    @Autowired
    private SearchEngine _searchEngine;

    @Autowired
    private VehicleDataPersistenceService _vehicleDataPersistenceService;

    @Autowired
    private VehicleNodeRepository _vehicleNodeRepository;

    @Autowired
    private TermRepository _termRepository;

    private List<String> _randomNodeNames;
    private List<String> _randomTermNames;
    private Random _random;


    @Before
    public void setUp() throws Exception {
        _vehicleNodeRepository.deleteAll();
        _termRepository.deleteAll();
        _random = new Random();
        createRandomizedTestDataSet();
    }

    @Test
    public void testAverageLatencyFor1000Requests() throws Exception {
        long averageTime = 0;
        final Stopwatch stopwatch = Stopwatch.createUnstarted();
        for (String term : _randomTermNames) {
            stopwatch.start();
            _searchEngine.search(term);
            stopwatch.stop();
            averageTime += stopwatch.elapsed(TimeUnit.MILLISECONDS);
            stopwatch.reset();
        }
        averageTime = averageTime / _randomTermNames.size();
        System.out.println("Average Latency for " + _randomTermNames.size() + " requests: " + averageTime + "ms");

        assertThat(averageTime, lessThan(100L));
    }

    private void createRandomizedTestDataSet() {
        _randomNodeNames = new ArrayList<>();
        _randomTermNames = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            _randomNodeNames.add(RandomStringUtils.random(12, true, true));
            _randomTermNames.add(RandomStringUtils.random(6, true, true));
        }
        for (String nodeName : _randomNodeNames)
            _vehicleDataPersistenceService.tokenizeAndSave(vehicleNodeWithName(nodeName), createSetWithRandomTerms());
    }

    private Set<String> createSetWithRandomTerms() {
        final List<String> strings = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            strings.add(_randomTermNames.get(_random.nextInt(1000)));
        }
        return new HashSet<>(strings);
    }

    private static VehicleNode vehicleNodeWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }
}