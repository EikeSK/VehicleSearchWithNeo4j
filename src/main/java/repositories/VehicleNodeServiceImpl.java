package repositories;

import domain.VehicleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import support.VehicleNodeSearchQuery;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VehicleNodeServiceImpl implements VehicleNodeService {

    private final VehicleNodeRepository _vehicleNodeRepository;

    public VehicleNodeServiceImpl(final VehicleNodeRepository vehicleNodeRepository) {
        _vehicleNodeRepository = vehicleNodeRepository;
    }

    @Override
    public List<VehicleNode> findNodesByQuery(final VehicleNodeSearchQuery searchQuery) {
        final List<VehicleNode> resultList = new ArrayList<>();
        final Iterable<VehicleNode> nodesByQuery = _vehicleNodeRepository.findNodesByQuery(searchQuery);
        for (VehicleNode resultNode : nodesByQuery) {
            resultList.add(resultNode);
        }
        return resultList;
    }
}