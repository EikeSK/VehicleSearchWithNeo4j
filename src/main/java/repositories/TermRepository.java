package repositories;

import domain.Term;
import org.springframework.data.repository.CrudRepository;

/**
 * Repräsentiert ein Repository für Spring Data Neo4j zum Zugriff auf die Datenbank für Objekte vom Typ Term
 */
public interface TermRepository extends CrudRepository<Term, String>, TermRepositoryCustom {

    /**
     * Liefert einen Suchbegriff vom Typ Term anhand eines Namens
     *
     * @param name der Name des Suchbegriffs
     * @return der Suchbegriff, der anhand des Namens gefunden wurde
     */
    public Term findByName(String name);
}
