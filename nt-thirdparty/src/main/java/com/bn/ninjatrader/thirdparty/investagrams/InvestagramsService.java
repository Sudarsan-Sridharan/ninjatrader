package com.bn.ninjatrader.thirdparty.investagrams;

import com.bn.ninjatrader.model.entity.DailyQuote;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Brad on 6/25/16.
 */
@Singleton
public class InvestagramsService {

  private static final Logger log = LoggerFactory.getLogger(InvestagramsService.class);

  @Inject
  private DailyPriceDownloader dailyPriceDownloader;

  @Inject
  private HistoricalPriceDownloader historicalPriceDownloader;

  public List<DailyQuote> getDailyQuotes() {
    return dailyPriceDownloader.download();
  }

  public List<DailyQuote> getHistoricalQuotesForAllStocks() {
    return historicalPriceDownloader.download();
  }

  public static void main(String args[]) throws IOException, ExecutionException, InterruptedException {
    InvestagramsService service = new InvestagramsService();
    service.getHistoricalQuotesForAllStocks();
  }
}
