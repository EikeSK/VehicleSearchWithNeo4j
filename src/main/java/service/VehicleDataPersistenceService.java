package service;

import domain.VehicleNode;

import java.util.Set;

public interface VehicleDataPersistenceService {

    public void tokenizeAndSave(VehicleNode vehicleNode);

    public void tokenizeAndSave(VehicleNode vehicleNode, Set<String> additionalMetaData);
}
