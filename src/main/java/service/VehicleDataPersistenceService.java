package service;

import domain.NodeMetaData;
import domain.VehicleNode;

import java.util.Map;
import java.util.Set;

public interface VehicleDataPersistenceService {

    public void tokenizeAndSave(VehicleNode vehicleNode);

    public void tokenizeAndSave(VehicleNode vehicleNode, Set<NodeMetaData> additionalMetaData);

    public void tokenizeAndSaveBatch(final Map<VehicleNode, Set<NodeMetaData>> batchData);
}
