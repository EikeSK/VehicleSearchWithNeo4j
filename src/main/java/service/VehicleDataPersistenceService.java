package service;

import domain.VehicleNode;
import support.VehicleMetaData;

import java.util.Map;

public interface VehicleDataPersistenceService {

    public void save(VehicleNode vehicleNode);

    public void save(VehicleNode vehicleNode, VehicleMetaData additionalMetaData);

    public void saveBatch(final Map<VehicleNode, VehicleMetaData> batchData);
}
