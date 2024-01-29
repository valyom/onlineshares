package online.pok.impl;

import java.time.LocalDate;

public class UrlFromLocalDate {
    static String getUrl(LocalDate ld) {
        return String.format("%s%04d-%02d-%02d", PokReaderDefines.URL_BASE, ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
    }

}
