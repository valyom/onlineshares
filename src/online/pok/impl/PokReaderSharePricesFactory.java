package online.pok.impl;

import online.bnb.BnbGoldRates;
import online.bnb.BnbRates;
import online.bnb.BnbUsdRates;
import online.pok.contract.PokSharesAtDate;

public class PokReaderSharePricesFactory<T>  {

  public  static PokSharesAtDate  create (String fund) {
      if ("dpf".compareToIgnoreCase(fund) == 0) return new PokReaderDpfSharePrices();
      if ("upf".compareToIgnoreCase(fund) == 0) return new PokReaderUpfSharePrices();
      if ("ppf".compareToIgnoreCase(fund) == 0) return new PokReaderPpfSharePrices();
      if ("bnb".compareToIgnoreCase(fund) == 0) return new BnbRates();
      if ("usd".compareToIgnoreCase(fund) == 0) return new BnbUsdRates();
      if ("xau".compareToIgnoreCase(fund) == 0) return new BnbGoldRates();

      return new PokReaderAllSharePrices();
  }

}
