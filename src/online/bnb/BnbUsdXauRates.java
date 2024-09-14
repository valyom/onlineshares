package online.bnb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BnbUsdXauRates  extends BnbRates {
    final public static String SECURITY =  "Злато";

    @Override
    public  Set<String> security() {
        return new HashSet<>(Arrays.asList(new String []{BnbUsdRates.SECURITY, SECURITY})); 
    }
}
