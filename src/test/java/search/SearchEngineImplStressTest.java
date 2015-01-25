package search;

import com.google.common.base.Stopwatch;
import config.ProductiveContext;
import domain.VehicleNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repositories.TermRepository;
import repositories.VehicleNodeRepository;
import service.VehicleDataPersistenceServiceImpl;
import support.VehicleMetaData;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static support.TestUtils.vehicleNodeWithName;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductiveContext.class})
public class SearchEngineImplStressTest {

    @Autowired
    private SearchEngine _searchEngine;

    @Autowired
    private VehicleDataPersistenceServiceImpl _vehicleDataPersistenceService;

    @Autowired
    private VehicleNodeRepository _vehicleNodeRepository;

    @Autowired
    private TermRepository _termRepository;

    private final List<String> _randomNodeNames = new ArrayList<>();
    private final List<String> _randomTermNames = new ArrayList<>();
    private Random _random;


    @Before
    public void setUp() throws Exception {
        _vehicleNodeRepository.deleteAll();
        _termRepository.deleteAll();
        _random = new Random();
        createRandomizedTestDataSet();
    }


    @Test
    public void stressTest() throws Exception {
        final List<Thread> threads = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            Runnable stressTask = new StressThread(i + 1, _searchEngine);
            executor.execute(stressTask);
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);

    }

    private void createRandomizedTestDataSet() {

        for (int i = 0; i < 1000; i++) {
            _randomNodeNames.add(RandomStringUtils.random(12, true, true));
            _randomTermNames.add(RandomStringUtils.random(6, true, true));
        }
        final Map<VehicleNode, VehicleMetaData> batch = new HashMap<>();
        for (String nodeName : _randomNodeNames) {
            batch.put(vehicleNodeWithName(nodeName), createMetaDataWithSetWithRandomTerms());
        }
        _vehicleDataPersistenceService.saveBatch(batch);
    }

    private VehicleMetaData createMetaDataWithSetWithRandomTerms() {
        final List<String> strings = new ArrayList<>();
        final VehicleMetaData vehicleMetaData = new VehicleMetaData();
        for (int i = 0; i < 8; i++) {
            strings.add(_randomTermNames.get(_random.nextInt(1000)));
        }
        vehicleMetaData.setAdditionalMetaData(new HashSet<>(strings));
        return vehicleMetaData;
    }

    public class StressThread implements Runnable {

        private int _threadNumber;
        private SearchEngine _searchEngine;

        public StressThread(final int threadNumber, final SearchEngine searchEngine) {
            _threadNumber = threadNumber;
            _searchEngine = searchEngine;
        }

        @Override
        public void run() {
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
            System.out.println("Thread " + _threadNumber + ": Average Latency for " + _randomTermNames.size() + " requests: " + averageTime + "ms");
        }
    }
}
