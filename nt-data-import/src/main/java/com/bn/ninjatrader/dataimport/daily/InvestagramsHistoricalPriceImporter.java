package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.bn.ninjatrader.thirdparty.investagrams.InvestagramsService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Brad on 4/28/16.
 */
@Singleton
public class InvestagramsHistoricalPriceImporter extends AbstractDailyPriceImporter{

  private static final Logger LOG = LoggerFactory.getLogger(InvestagramsHistoricalPriceImporter.class);

  @Inject
  private InvestagramsService investagramsService;

  @Override
  protected List<DailyQuote> provideDailyQuotes(final LocalDate date) {
    return provideDailyQuotes();
  }

  protected List<DailyQuote> provideDailyQuotes() {
    return investagramsService.getHistoricalQuotesForAllStocks();
  }

  @Override
  public void importData(final Collection<LocalDate> dates) {
    for (Map.Entry<String, List<Price>> symbolPriceList : toPriceMap(provideDailyQuotes()).entrySet()) {
      LOG.debug("Saved prices for {}", symbolPriceList.getKey());
      priceDao.save(SaveRequest.save(symbolPriceList.getKey())
          .timeFrame(TimeFrame.ONE_DAY)
          .values(symbolPriceList.getValue()));
    }
  }

  private Map<String, List<Price>> toPriceMap(final List<DailyQuote> dailyQuotes) {
    final Map<String, List<Price>> symbolMap = Maps.newHashMap();
    for (final DailyQuote dailyQuote : dailyQuotes) {
      final String symbol = dailyQuote.getSymbol();
      if (symbolMap.get(symbol) == null) {
        symbolMap.put(symbol, Lists.newArrayList());
      }
      symbolMap.get(symbol).add(dailyQuote.getPrice());
    }
    return symbolMap;
  }
}
