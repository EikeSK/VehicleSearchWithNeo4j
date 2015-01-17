package service;

import domain.VehicleNode;
import support.VehicleMetaData;

import java.util.Map;

public interface VehicleDataPersistenceService {

    public void tokenizeAndSave(VehicleNode vehicleNode);

    public void tokenizeAndSave(VehicleNode vehicleNode, VehicleMetaData additionalMetaData);

    public void tokenizeAndSaveBatch(final Map<VehicleNode, VehicleMetaData> batchData);
}
