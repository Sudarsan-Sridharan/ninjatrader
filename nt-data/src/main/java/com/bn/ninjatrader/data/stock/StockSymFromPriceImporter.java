package com.bn.ninjatrader.data.stock;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.StockDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 6/11/16.
 */
@Singleton
public class StockSymFromPriceImporter {
  private static final Logger log = LoggerFactory.getLogger(StockSymFromPriceImporter.class);

  @Inject
  private PriceDao priceDao;

  @Inject
  private StockDao stockDao;

  public void importData() {
    List<String> symbols = priceDao.findAllSymbols();

    for (String symbol : symbols) {
      Stock stock = new Stock(symbol, "");
      stockDao.save(stock);
    }
  }
}
