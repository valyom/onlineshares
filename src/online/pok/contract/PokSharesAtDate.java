package online.pok.contract;

import java.time.LocalDate;

public interface PokSharesAtDate  {
    void sharePricesValidAtDate(LocalDate date) ;
    void sharePricesPeriod(LocalDate date, LocalDate dateEnd) ;
}
