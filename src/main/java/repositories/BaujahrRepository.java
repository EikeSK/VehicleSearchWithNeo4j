package repositories;

import domain.Baujahr;
import org.springframework.data.repository.CrudRepository;

public interface BaujahrRepository extends CrudRepository<Baujahr, String> {

    public Baujahr findByValue(double value);
}
