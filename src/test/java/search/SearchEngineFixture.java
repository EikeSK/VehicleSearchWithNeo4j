package search;

import config.ProductiveContext;
import fit.ColumnFixture;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SearchEngineFixture extends ColumnFixture {

    private SearchEngine _searchEngine;
    private String searchQuery;

    public SearchEngineFixture() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ProductiveContext.class);
        for (String test : ctx.getBeanDefinitionNames()) {
            System.out.println(test);       //TODO: not working because FitNesse seems to be unable to start spring context
        }
        _searchEngine = (SearchEngine) ctx.getBean("searchEngine");
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String search() {
        _searchEngine.search(searchQuery);
        return "foo";
    }
}