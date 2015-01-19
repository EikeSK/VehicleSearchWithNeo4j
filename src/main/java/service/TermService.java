package service;


import java.util.Collection;

public interface TermService {

    public Collection<String> findTermNamesByIncompleteName(final String name);
}
