package com.bn.ninjatrader.data.daily;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.thirdparty.investagrams.InvestagramsService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Brad on 4/28/16.
 */
@Singleton
public class InvestagramsHistoricalPriceImporter extends AbstractDailyPriceImporter{

  private static final Logger log = LoggerFactory.getLogger(InvestagramsHistoricalPriceImporter.class);

  @Inject
  private InvestagramsService investagramsService;

  @Override
  protected List<DailyQuote> provideDailyQuotes() {
    return investagramsService.getHistoricalQuotesForAllStocks();
  }

  @Override
  public void importData() throws IOException {
    for (Map.Entry<String, List<Price>> symbolPriceList : toPriceMap(provideDailyQuotes()).entrySet()) {
      log.debug("Saved prices for {}", symbolPriceList.getKey());
      priceDao.save(symbolPriceList.getKey(), symbolPriceList.getValue());
    }
  }

  private Map<String, List<Price>> toPriceMap(List<DailyQuote> dailyQuotes) {
    Map<String, List<Price>> symbolMap = Maps.newHashMap();
    for (DailyQuote dailyQuote : dailyQuotes) {
      String symbol = dailyQuote.getSymbol();
      if (symbolMap.get(symbol) == null) {
        symbolMap.put(symbol, Lists.newArrayList());
      }
      symbolMap.get(symbol).add(dailyQuote.getPrice());
    }
    return symbolMap;
  }

  public static void main(String args[]) throws IOException {
    Injector injector = Guice.createInjector(
        new NtModelModule()
    );

    InvestagramsHistoricalPriceImporter app = injector.getInstance(InvestagramsHistoricalPriceImporter.class);
    app.importData();
  }
}
