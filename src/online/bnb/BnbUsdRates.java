package online.bnb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BnbUsdRates extends BnbRates {
    final public static String SECURITY = "Щатски долар";
   
    @Override
    public  Set<String> security() {
        return new HashSet<>(Arrays.asList(SECURITY));
    }
}
