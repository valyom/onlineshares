package online.pok.contract;

import java.time.LocalDate;
import java.util.List;

public interface PokSharesAtDate  {
    List<Double> sharePricesValidAtDate(LocalDate date) ;
    void sharePricesPeriod(LocalDate date, LocalDate dateEnd) ;
}
