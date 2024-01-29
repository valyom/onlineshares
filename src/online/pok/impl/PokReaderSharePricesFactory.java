package online.pok.impl;

import online.bnb.BnbUSdRates;
import online.pok.contract.PokSharesAtDate;

public class PokReaderSharePricesFactory<T>  {

  public  static PokSharesAtDate  create (String fund) {
      if ("dpf".compareToIgnoreCase(fund) == 0) return new PokReaderDpfSharePrices();
      if ("upf".compareToIgnoreCase(fund) == 0) return new PokReaderUpfSharePrices();
      if ("ppf".compareToIgnoreCase(fund) == 0) return new PokReaderPpfSharePrices();
      if ("usd".compareToIgnoreCase(fund) == 0) return new BnbUSdRates();

      return new PokReaderAllSharePrices();
  }

}
