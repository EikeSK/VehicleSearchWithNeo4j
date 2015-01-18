package support;

import java.util.HashSet;
import java.util.Set;

public class VehicleMetaData {

    private Set<String> _additionalMetaData = new HashSet<>();
    private int _baujahrFrom;
    private int _baujahrTo;

    public Set<String> getAdditionalMetaData() {
        return _additionalMetaData;
    }

    public void setAdditionalMetaData(final Set<String> additionalMetaData) {
        _additionalMetaData.addAll(additionalMetaData);
    }

    public int getBaujahrFrom() {
        return _baujahrFrom;
    }

    public void setBaujahrFrom(final int baujahrFrom) {
        _baujahrFrom = baujahrFrom;
    }

    public int getBaujahrTo() {
        return _baujahrTo;
    }

    public void setBaujahrTo(int baujahrTo) {
        _baujahrTo = baujahrTo;
    }
}
