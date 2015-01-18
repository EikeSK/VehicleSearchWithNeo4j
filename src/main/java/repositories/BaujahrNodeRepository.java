package repositories;

import domain.Baujahr;
import org.springframework.data.repository.CrudRepository;

public interface BaujahrNodeRepository extends CrudRepository<Baujahr, String> {

    public Baujahr findByValue(double value);
}
