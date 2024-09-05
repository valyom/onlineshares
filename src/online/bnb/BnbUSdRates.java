package online.bnb;

public class BnbUsdRates extends BnbRates {
    final public static String SECURITY = "Щатски долар";
   
    @Override
    public String security() {
        return SECURITY;
    }
}
