package com.bn.ninjatrader.server.service;

import com.bn.ninjatrader.calculator.IchimokuCalculator;
import com.bn.ninjatrader.calculator.parameter.IchimokuParameters;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.model.dao.MeanDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.StockDao;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 5/3/16.
 */
@Singleton
public class IchimokuService {

  private static final Logger log = LoggerFactory.getLogger(IchimokuService.class);

  @Inject
  private StockDao stockDao;

  private PriceDao priceDao;

  private MeanDao meanDao;

  @Inject
  private IchimokuCalculator ichimokuCalculator;

  @Inject
  public IchimokuService(PriceDao priceDao, MeanDao meanDao) {
    this.priceDao = priceDao;
    this.meanDao = meanDao;
  }

  public List<Ichimoku> getIchimoku(String symbol, LocalDate fromDate, LocalDate toDate) {
    int numOfPastMonths = getNumOfPastMonths();
    List<Price> prices = priceDao.findByDateRange(symbol, fromDate.minusMonths(numOfPastMonths), toDate);
    List<Value> tenkanList = meanDao.findByDateRange(symbol, 9, fromDate.minusMonths(numOfPastMonths), toDate);
    List<Value> kijunList = meanDao.findByDateRange(symbol, 26, fromDate.minusMonths(numOfPastMonths), toDate);
    List<Value> senKouBList = meanDao.findByDateRange(symbol, 52, fromDate.minusMonths(numOfPastMonths), toDate);

    List<Ichimoku> ichimokuList = ichimokuCalculator.calc(
        IchimokuParameters.builder()
            .priceList(prices)
            .tenkanList(tenkanList)
            .kijunList(kijunList)
            .senkouBList(senKouBList)
            .chickouShiftBackPeriods(26)
            .senkouShiftForwardPeriods(26)
            .build());

    DateObjUtil.trimToDateRange(ichimokuList, fromDate, toDate);

    return ichimokuList;
  }

  public List<Stock> getStocksWithKijunCross() {
    LocalDate fromDate = LocalDate.now().minusMonths(1);
    LocalDate toDate = LocalDate.now();

    List<Stock> hits = Lists.newArrayList();

    for (Stock stock : stockDao.find()) {

      List<Value> tenkanList = meanDao.findByDateRange(stock.getSymbol(), 9, fromDate, toDate);
      List<Value> kijunList = meanDao.findByDateRange(stock.getSymbol(), 26, fromDate, toDate);
      List<Price> priceList = priceDao.findByDateRange(stock.getSymbol(), fromDate, toDate);

      // Skip does w/ not enough data
      if (tenkanList.isEmpty() || priceList.isEmpty() || kijunList.isEmpty()) {
        continue;
      }

      // Get most recent data
      double tenkan = tenkanList.get(tenkanList.size() - 1).getValue();
      double kijun = kijunList.get(kijunList.size() - 1).getValue();
      double close = priceList.get(priceList.size() - 1).getClose();

      if (close > tenkan && close > kijun && tenkan > kijun) {
        hits.add(stock);
      }
    }
    return hits;
  }

  protected int getNumOfPastMonths() {
    return 3; // need enough months of past data to calculate present ichimoku
  }
}
