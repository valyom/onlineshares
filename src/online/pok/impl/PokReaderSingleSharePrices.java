package online.pok.impl;

import java.time.LocalDate;
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
        DatesIterator it = new DatesIterator(date, GlobalDefines.MAX_DAYS_BACK);

        this.printCaptionIfNot();
        for (LocalDate d = it.first(); !atExactDate(d) && it.isInRange(); d = it.next());
    }

    // interface implementation
    public void sharePricesPeriod(LocalDate date, LocalDate dateEnd) {
        this.printCaptionIfNot();

        DatesIterator it = new DatesIterator(date, dateEnd);
        for (LocalDate d = it.first(); it.isInRange(); d = it.next()) {
            this.atExactDate(d);
        }
    }

    abstract protected String getFundSurchKeyWord();

    private boolean atExactDate(LocalDate ld) {
        String[] rows = PageTextDownloader.download(UrlFromLocalDate.getUrl(ld), null, 0, null).split("},");

        if (rows.length < PokReaderDefines.POKS.length - 1) {
            return false;
        }

        final String fundType = this.getFundSurchKeyWord();
        final int fundTypeStrLen = fundType.length();

        System.out.print(GlobalDefines.date2Str(ld));

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

            System.out.print(String.format(GlobalDefines.DOUBLE_TO_STRING_FORMAT_SINGLE,  strToDouble(curRes)));
        }

        System.out.println();
        return true;
    }

    private double strToDouble(String val) {
        double v = 1.;
        try {
            v = Double.parseDouble(val);
        } catch (NumberFormatException e) {

        }
        return v;
    }

    private void printCaptionIfNot() {
        if (!captionPrinted) {
            captionPrinted = true;
            System.out.println(String.format("      Date%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s",
                    "Doverie", "Saglasie", "Rodina", "Allianz", "NN", "Sila", "Badeshte", "Toplina", "POI", "DalBogg",
                    "VOLIDEX"));
        }
    }

}
