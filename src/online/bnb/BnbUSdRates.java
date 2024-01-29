package online.bnb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import online.dates.DatesIterator;
import online.pagereader.PageTextDownloader;
import online.pok.contract.PokSharesAtDate;
import online.pok.defines.GlobalDefines;

public class BnbUSdRates implements PokSharesAtDate {
    final private static int STARTING_LINE_INDEX = 2595;
    final private static String urlDateFormat = "https://www.bnb.bg/Statistics/StExternalSector/StExchangeRates/StERForeignCurrencies/index.htm?downloadOper=&group1=first&firstDays=%02d&firstMonths=%02d&firstYear=%04d&search=true&showChart=false&showChartButton=false";
    final private static String usdCaption = "Щатски долар";

    public List<Double> sharePricesValidAtDate(LocalDate date) {
        DatesIterator it = new DatesIterator(date,  GlobalDefines.MAX_DAYS_BACK);
        List<Double> r = null;

        for (LocalDate d = it.first(); r == null && it.isInRange(); d = it.next()) {
            r = processDate(d);
        }

        return r;
    };

    public void sharePricesPeriod(LocalDate date, LocalDate date1) {
        DatesIterator it = new DatesIterator(date, date1);
        for (LocalDate d = it.first(); it.isInRange(); d = it.next()) {
            processDate(d);
        }
    }

    private List<Double> processDate(LocalDate date) {
        List<Double> res = null;
        String eol = "" + (char) 13;
        String[] rows = PageTextDownloader.download(
                String.format(BnbUSdRates.urlDateFormat, date.getDayOfMonth(), date.getMonthValue(), date.getYear()),
                "TBMCookie_437004684443784604=39071001705664178s2Ctv48H+Qzv31lE3Ey+MlZk+S4=",
                STARTING_LINE_INDEX, eol)
                .split(eol);

        for (int i = 0; i < rows.length; i++) {
            if (rows[i].contains(usdCaption)) {
                String[] usdAttr = rows[i + 3].split("[<>]");
                System.out.println(String.format("%02d.%02d.%04d  :  %-7s", date.getDayOfMonth(), date.getMonthValue(),
                        date.getYear(), usdAttr[2]));

                res = new ArrayList<>();
                res.add(Double.parseDouble(usdAttr[2].replaceAll(",", ".")));

                return res;
            }

        }
        return res;
    }
}
