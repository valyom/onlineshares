package online.pok.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import online.dates.DatesIterator;
import online.pagereader.PageTextDownloader;
import online.pok.contract.PokSharesAtDate;
import online.pok.defines.GlobalDefines;
import online.ringimpl.RingIndexFactory;
import online.ringimpl.contract.RingIndex;

public abstract class PokReaderSingleSharePrices implements PokSharesAtDate {
    private boolean captionPrinted;

    // package scope only allowed
    PokReaderSingleSharePrices() {
        captionPrinted = false;
    }

    // interface implementation
    public void sharePricesValidAtDate(LocalDate date) {
        boolean done = false;
        DatesIterator it = new DatesIterator(date, GlobalDefines.MAX_DAYS_BACK);

        this.printCaptionIfNot();
        for (LocalDate d = it.first(); !done  && it.isInRange(); d = it.next()) {

            done = dumpRow(d);
        }
    }

    // interface implementation
    public void sharePricesPeriod(LocalDate date, LocalDate dateEnd) {
        this.printCaptionIfNot();

        DatesIterator it = new DatesIterator(date, dateEnd);
        for (LocalDate d = it.first(); it.isInRange(); d = it.next()) {
            this.dumpRow(d);
        }
    }

    // use polymorphism to provide fund type
    abstract protected String getFundSurchKeyWord();

    private List<Double> atExactDate(LocalDate ld) {
        String[] rows = PageTextDownloader.download(UrlFromLocalDate.getUrl(ld), null, 0, null).split("},");

        if (rows.length < PokReaderDefines.POKS.length - 1) {
            return null;
        }

        final String fundType = this.getFundSurchKeyWord(); // use polymorphism to process the proper fund
        final int fundTypeStrLen = fundType.length();

        List<Double> vals = new ArrayList<>();

        for (int pokIdx = 0; pokIdx < PokReaderDefines.POKS.length; pokIdx++) {
            RingIndex ri = RingIndexFactory.create(pokIdx <rows.length ?pokIdx  : 0, rows.length); // optimization
            String curRes = "1.0";

            for (int i = ri.getNext(); i >= 0; i = ri.getNext()) {
                String row = rows[i];
                if (row.contains(PokReaderDefines.POKS[pokIdx])) {
                    int startIdx = row.indexOf(fundType) + fundTypeStrLen;
                    curRes = row.substring(startIdx, row.indexOf(",", startIdx + 2));

                    break;
                }
            }
            vals.add(pokIdx, strToDouble(curRes));
        }

        return vals;
    }

    private void printCaptionIfNot() {
        if (!captionPrinted) {
            captionPrinted = true;
            System.out.println(String.format("      Date %7s   %7s   %7s   %7s   %7s   %7s  %7s   %7s   %7s   %7s   %7s",
                    "Doverie", "Saglasie", "Rodina", "Allianz", "NN", "Sila", "Badeshte", "Toplina", "POI", "DalBogg",
                    "VOLIDEX"));
        }
    }

    private double strToDouble(String val) {
        double v = 1.;
        try {
            v = Double.parseDouble(val);
        } catch (NumberFormatException e) {

        }
        return v;
    }

    private boolean dumpRow(LocalDate ld) {
        List<Double> vals = this.atExactDate(ld);
        if (vals == null) {
            return false;
        }

        System.out.print(String.format("%02d.%02d.%04d  ", ld.getDayOfMonth(), ld.getMonthValue(), ld.getYear()));
        for (double val : vals) {
            System.out.print(String.format("%7.5f   ", val));
        }

        System.out.println();

        return true;
    }
}
