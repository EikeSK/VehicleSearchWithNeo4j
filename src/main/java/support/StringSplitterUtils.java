package support;

import com.google.common.base.Splitter;
import domain.VehicleNode;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Ascii.toLowerCase;

public class StringSplitterUtils {

    public static Set<String> tokenize(final VehicleNode vehicleNode) {
        String toTokenize = null;
        if (vehicleNode != null) {
            toTokenize = vehicleNode.getName();
        }
        return tokenize(toTokenize);
    }

    public static Set<String> tokenize(final String string) {
        final Set<String> tokens = new HashSet<>();
        if (string != null) {
            final Splitter splitter = Splitter.on(" ").omitEmptyStrings().trimResults();
            final Iterable<String> tokenIterable = splitter.split(string);
            for (String token : tokenIterable) {
                tokens.add(toLowerCase(token));
            }
        }
        return tokens;
    }
}
