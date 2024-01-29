package online.pok.impl;

import java.time.LocalDate;
import java.util.List;
import online.dates.DatesIterator;
import online.pagereader.PageTextDownloader;
import online.pok.contract.PokSharesAtDate;
import online.pok.defines.GlobalDefines;
import online.ringimpl.RingIndexFactory;
import online.ringimpl.contract.RingIndex;

public class PokReaderAllSharePrices implements PokSharesAtDate {
    private final int MIN_NUM_FUNDS = PokReaderDefines.POKS.length - 1; // not a mistake - allow one fund less

    // package scope only allowed
    PokReaderAllSharePrices() {

    }

    public List<Double> sharePricesValidAtDate(LocalDate date ) {
        DatesIterator it = new DatesIterator(date, GlobalDefines.MAX_DAYS_BACK);

        for (LocalDate d = it.first(); it.isInRange(); d = it.next()) {
            String[] rows = getOnlileData(d);
            if (rows.length >= MIN_NUM_FUNDS) {
                processData(rows, d);
                return null;
            }
        }

        return null;
    }


    public void sharePricesPeriod(LocalDate date, LocalDate date1) {
        DatesIterator it = new DatesIterator(date, date1);
        for (LocalDate d = it.first(); it.isInRange(); d = it.next()) {
            processData(getOnlileData(d), d);
        }
    }

    private void processData(String[] rows, LocalDate date) {
        final String invalidValue = "1.00000";
        if (rows.length < MIN_NUM_FUNDS) {
            return;
        }

        System.out.println(String.format("%31s : %04d-%02d-%02d", "Reported Date",
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth()));
        System.out.println(String.format("%31s : %7s   %7s   %7s", "POK", "DPF", "PPF", "UPF"));

        for (int pokIdx = 0; pokIdx < PokReaderDefines.POKS.length; pokIdx++) {
            String ppfValue = invalidValue;
            String upfValue = invalidValue;
            String dpfValue = invalidValue;
            RingIndex ri = RingIndexFactory.create(pokIdx, rows.length);

            for (int i = ri.getNext(); i >= 0; i = ri.getNext()) {
                String row = rows[i];
                if (row.contains(PokReaderDefines.POKS[pokIdx])) {
                    ppfValue = sharePrice(row, PokReaderDefines.FUND_PPF_KEY);
                    upfValue = sharePrice(row, PokReaderDefines.FUND_UPF_KEY);
                    dpfValue = sharePrice(row, PokReaderDefines.FUND_DPF_KEY);

                    break;
                }
            }

            System.out.println(String.format("%31s : %-7s;  %-7s;  %-7s", PokReaderDefines.POKS[pokIdx], dpfValue,
                    ppfValue, upfValue));
        }

    }

    private String[] getOnlileData(LocalDate date) {
        return PageTextDownloader.download(UrlFromLocalDate.getUrl(date), null, 0, null).split("},");
    }

    private String sharePrice(String row, String fundType) {
        final String comma = ",";
        String reducedRow = row.substring(row.indexOf(fundType) + fundType.length());
        return reducedRow.substring(0, reducedRow.indexOf(comma));
    }

}
