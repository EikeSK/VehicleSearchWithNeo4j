package repositories;

import domain.Term;

/**
 * Repräsentiert eine Erweiterung des Spring Data Neo4j Repositories für Suchbegriffe vom Typ Term
 */
public interface TermRepositoryCustom {

    /**
     * Liefert anhand eines unvollständigen Namens alle Suchbegriffen, die mit der angegebenen Zeichenkette beginnen
     *
     * @param name die Zeichenkette, mit der die Suchbegriffe beginnen.
     * @return eine Liste von Suchbegriffen, die mit der angegebenen Zeichenkette beginnen.
     */
    public Iterable<Term> findByIncompleteName(final String name);

}
