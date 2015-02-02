package support;

import domain.VehicleNode;

import java.util.Set;

/**
 * Eine Sammlung statischer Hilfsmethoden zum Erstellen von Werteobjekten in Testmethoden
 */
public class TestUtils {

    public static VehicleNode vehicleNodeWithName(final String name) {
        final VehicleNode vehicleNode = new VehicleNode();
        vehicleNode.setName(name);
        return vehicleNode;
    }

    public static VehicleMetaData vehicleMetaDataWithTerms(final Set<String> additionalTerms) {
        return vehicleMetaDataWithTermsAndBaujahr(additionalTerms, 0, 0);
    }

    public static VehicleMetaData vehicleMetaDataWithTermsAndBaujahr(final Set<String> additionalTerms, int baujahr) {
        return vehicleMetaDataWithTermsAndBaujahr(additionalTerms, baujahr, 0);
    }

    public static VehicleMetaData vehicleMetaDataWithTermsAndBaujahr(final Set<String> additionalTerms, int fromBaujahr, int toBaujahr) {
        final VehicleMetaData vehicleMetaData = new VehicleMetaData();
        vehicleMetaData.setAdditionalMetaData(additionalTerms);
        vehicleMetaData.setBaujahrFrom(fromBaujahr);
        vehicleMetaData.setBaujahrTo(toBaujahr);
        return vehicleMetaData;
    }
}
