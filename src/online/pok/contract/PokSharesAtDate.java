package online.pok.contract;

import java.time.LocalDate;
import java.util.List;

public interface PokSharesAtDate  {
    void sharePricesValidAtDate(LocalDate date) ;
    void sharePricesPeriod(LocalDate date, LocalDate dateEnd) ;
}
