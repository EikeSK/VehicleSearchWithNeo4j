package support;

import domain.NodeMetaData;

public class TestUtils {

    public static NodeMetaData metaDataWith(String name) {
        final NodeMetaData metaData = new NodeMetaData();
        metaData.setName(name);
        return metaData;
    }

    public static NodeMetaData metaDataWith(String name, String unit) {
        final NodeMetaData metaData = metaDataWith(name);
        metaData.setUnit(unit);
        return metaData;
    }
}
