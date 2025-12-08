package online.bnb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import online.pagereader.PageTextDownloader;

public class BnbGoldRates extends BnbRates {
   // final public static String SECURITY =  "Злато";
   final public static String SECURITY = "Щатски долар";
   
    // private static void redirectSystemOut(String fileName)   {
    //     File file = new File(fileName);
    //     try (PrintStream fileOut = new PrintStream(file)) {
    //         System.setOut(fileOut);
    //     } catch (FileNotFoundException e) {  }
    // }

    // private static void restoreSystemOut() {
    //     System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    // }
    private static final String URL_GOLD_LAST_PRICE = "https://markets.businessinsider.com/commodities/gold-price";// "https://www.goldprice.o
 
    private double goldGurPrice( ) {
        //redirectSystemOut("./output");
        final String markBefore = "<span class=\"price-section__current-value\">";
        final String markAfter = "</span>";
        double result = 0.0;
        
        String ss = PageTextDownloader.download(URL_GOLD_LAST_PRICE, markBefore,null,1500, 5600, null).trim(); 
        //restoreSystemOut(); 
        int pos =  ss.indexOf(markBefore) + markBefore.length();
        if(pos >= 0){
            int pos2 = ss.indexOf(markAfter, pos);
            String prc = ss.substring(pos, pos2) ;
            prc = prc.replace(",", "").trim();
            result = Double.valueOf(prc);
            // System.out.println("1 oz gold price now :" + prc);
            // System.out.println("1 gramm gold price in leva now :" + 1.76998 * result/ 31.1);
        }
        //provideConverter

        return result;
    }
    
    @Override
    public  Set<String> security() {
        return new HashSet<>(Arrays.asList(BnbGoldRates.SECURITY));
    }

    private List<Function<String, String>> converterS = null;
    private Double goldNow = 0.;

    private void prepareConvertes () {
        goldNow = goldGurPrice( );
        converterS = new ArrayList<>();
        converterS.add(s -> String.format("%-7.2f",  goldNow ));
        converterS.add(s -> String.format("%-7.2f", Double.valueOf(s) * goldNow ));
        converterS.add(s -> String.format("%-7.2f", Double.valueOf(s) * goldNow / 31.1));
        provideConverter(converterS);
    }

    // @Override
    // public void sharePricesValidAtDate(LocalDate date) {
        
    //     super.sharePricesValidAtDate(date);
    // };

    // @Override
    // public void sharePricesPeriod(LocalDate date, LocalDate date1) {
        
    //     prepareConvertes ();
    //     super.sharePricesPeriod(date, date1);
    // }

    public BnbGoldRates() {
        super();
        prepareConvertes ();

        
        System.out.println("Gold price is the latest - implementation not finished");
    }
}
