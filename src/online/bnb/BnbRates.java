package online.bnb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import online.dates.DatesIterator;
import online.pagereader.PageTextDownloader;
import online.pok.contract.PokSharesAtDate;
import online.pok.defines.GlobalDefines;

public   class BnbRates implements PokSharesAtDate {
    final private static int STARTING_LINE_INDEX = 2635;
    final private static String urlDateFormat = "https://www.bnb.bg/Statistics/StExternalSector/StExchangeRates/StERForeignCurrencies/index.htm?downloadOper=&group1=first&firstDays=%02d&firstMonths=%02d&firstYear=%04d&search=true&showChart=false&showChartButton=false";
 
    public void sharePricesValidAtDate(LocalDate date) {
        DatesIterator it = new DatesIterator(date,  GlobalDefines.MAX_DAYS_BACK);
        boolean r = false;

        for (LocalDate d = it.first(); r == false && it.isInRange(); d = it.next()) {
            r = processDate(d);
        }

    };

    public void sharePricesPeriod(LocalDate date, LocalDate date1) {
        DatesIterator it = new DatesIterator(date, date1);
        for (LocalDate d = it.first(); it.isInRange(); d = it.next()) {
            processDate(d);
        }
    }

    // override this to specify what to extract 
    public  Set<String> security() {
        return new HashSet<>(Arrays.asList(new String []{BnbUsdRates.SECURITY})); 
    }

    protected void provideConverter  ( List<Function<String, String>> converters)  {
        convertWithUsd = converters;
    }

    private List<Function<String, String>> convertWithUsd = null; 

    protected boolean processDate(LocalDate date) {
        String eol = System.lineSeparator();
        String[] rows = PageTextDownloader.download(
                String.format(BnbRates.urlDateFormat, date.getDayOfMonth(), date.getMonthValue(), date.getYear()),
                "TBMCookie_437004684443784604=39071001705664178s2Ctv48H+Qzv31lE3Ey+MlZk+S4=",
                STARTING_LINE_INDEX,STARTING_LINE_INDEX+40, eol)
                .split(eol);
        
        Set<String> securitiesToFind = security();
        List<String> securitiesFound = new ArrayList<>(); 
 
        int succCnt = 0;
        for (int i = 0; succCnt <= securitiesToFind.size() && i < rows.length; i++) {
            for(String securitytoFind :securitiesToFind ){
                if (rows[i].contains(securitytoFind)){ // (usdCaption)) {
                    String[] securityAttr = rows[i + 3].split("[<>]");

                    securitiesFound.add(securityAttr[2]);
                    if(convertWithUsd != null){
                        for(Function<String, String> converter : convertWithUsd){ 
                            securitiesFound.add(converter.apply(securityAttr[2]));
                        }    
                    }  
                    ++succCnt;
                    i += 3;
                    securitiesToFind.remove(securitytoFind);
                    break;
                }
            }
        }
        if (!securitiesFound.isEmpty()){ 
            System.out.print(GlobalDefines.date2Str(date));
            for(String s : securitiesFound)
                System.out.print(String.format(";%-7s", s));
            
            System.out.println();
            return true;
        }
        
        return false;
    }
}
