import com.google.common.base.Stopwatch;
import config.ProductiveContext;
import domain.VehicleNode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import search.SearchEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Beispielanwendung zur Demonstration der Funktionalitäten der Suchschnittstelle
 */
public class VehicleSearchApplication implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ProductiveContext.class);
        SearchEngine searchEngine = ctx.getBean(SearchEngine.class);
        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        final Stopwatch stopwatch = Stopwatch.createUnstarted();
        boolean isRunning = true;
        do {
            System.out.println("\n\n1 - Start Search\n2 - Autocomplete\n3 - Exit");
            System.out.print("Choice: ");
            String menu = input.readLine();
            switch (menu) {
                case "1": {
                    System.out.print("Search input: ");
                    String searchInput = input.readLine();
                    stopwatch.start();
                    final Collection<VehicleNode> result = searchEngine.search(searchInput);
                    stopwatch.stop();
                    System.out.print("Results: ");
                    if (result.size() == 0) {
                        System.out.print("no results");
                    } else {
                        for (VehicleNode resultNode : result) {
                            System.out.print(resultNode.getName() + "; ");
                        }
                    }
                    System.out.println("\nElapsed time: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
                    stopwatch.reset();
                    break;
                }
                case "2": {
                    System.out.print("Autocomplete input: ");
                    String searchInput = input.readLine();
                    stopwatch.start();
                    final Collection<String> result = searchEngine.autocomplete(searchInput);
                    stopwatch.stop();
                    System.out.print("Results: ");
                    if (result.size() == 0) {
                        System.out.print("no results");
                    } else {
                        for (String autocompleteResult : result) {
                            System.out.print(autocompleteResult + "; ");
                        }
                    }
                    System.out.println("\nElapsed time: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
                    stopwatch.reset();

                    break;
                }
                case "3":
                    isRunning = false;
                    break;
            }
        } while (isRunning);
    }

    public static void main(String[] args) {
        SpringApplication.run(VehicleSearchApplication.class, args);
    }
}
