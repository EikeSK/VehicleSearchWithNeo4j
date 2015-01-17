package repositories;

import domain.BaujahrNode;
import org.springframework.data.repository.CrudRepository;

public interface BaujahrNodeRepository extends CrudRepository<BaujahrNode, String> {

    public BaujahrNode findByValue(double value);
}
