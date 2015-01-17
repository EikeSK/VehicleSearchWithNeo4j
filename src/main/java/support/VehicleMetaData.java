package support;

import java.util.HashSet;
import java.util.Set;

public class VehicleMetaData {

    private Set<String> _additionalMetaData = new HashSet<>();
    private int _baujahr;

    public Set<String> getAdditionalMetaData() {
        return _additionalMetaData;
    }

    public void setAdditionalMetaData(final Set<String> additionalMetaData) {
        _additionalMetaData.addAll(additionalMetaData);
    }

    public int getBaujahr() {
        return _baujahr;
    }

    public void setBaujahr(final int baujahr) {
        _baujahr = baujahr;
    }
}
