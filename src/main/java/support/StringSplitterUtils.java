package support;

import com.google.common.base.Splitter;
import domain.VehicleModel;

import java.util.HashSet;
import java.util.Set;

public class StringSplitterUtils {

    public static Set<String> tokenize(final VehicleModel vehicleModel) {
        String toTokenize = null;
        if (vehicleModel != null) {
            toTokenize = vehicleModel.getName();
        }
        return tokenize(toTokenize);
    }

    public static Set<String> tokenize(final String string) {
        final Set<String> tokens = new HashSet<>();
        if (string != null) {
            final Splitter splitter = Splitter.on(" ").omitEmptyStrings().trimResults();
            final Iterable<String> tokenIterable = splitter.split(string);
            for (String token : tokenIterable) {
                tokens.add(token);  // TODO: toLowerCase für einheitliche Terms in der Datenbank?
            }
        }
        return tokens;
    }
}
