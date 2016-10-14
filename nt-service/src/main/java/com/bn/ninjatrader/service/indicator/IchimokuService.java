package com.bn.ninjatrader.service.indicator;

import com.bn.ninjatrader.calculator.IchimokuCalculator;
import com.bn.ninjatrader.calculator.parameter.IchimokuParameters;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.model.dao.MeanDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.StockDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;

/**
 * @author bradwee2000@gmail.com
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

  public void fillFutureDates(List<Ichimoku> ichimokuList) {
    LocalDate lastDate = LocalDate.now();
    for (Ichimoku ichimoku : ichimokuList) {
      if (ichimoku.getDate() == null) {
        lastDate = DateUtil.nextWeekday(lastDate);
        ichimoku.setDate(lastDate);
      } else {
        lastDate = ichimoku.getDate();
      }
    }
  }

  public List<Ichimoku> find(FindRequest findRequest) {
    String symbol = findRequest.getSymbol();
    LocalDate fromDate = findRequest.getFromDate();
    LocalDate toDate = findRequest.getToDate();

    int numOfPastMonths = getNumOfPastMonths();
    LocalDate findMeanFromDate = fromDate.minusMonths(numOfPastMonths);

    List<Price> prices = priceDao.find(findSymbol(symbol).from(findMeanFromDate).to(toDate));
    List<Value> tenkanList = meanDao.find(findSymbol(symbol).period(9).from(findMeanFromDate).to(toDate));
    List<Value> kijunList = meanDao.find(findSymbol(symbol).period(26).from(findMeanFromDate).to(toDate));
    List<Value> senKouBList = meanDao.find(findSymbol(symbol).period(52).from(findMeanFromDate).to(toDate));

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

    fillFutureDates(ichimokuList);

    return ichimokuList;
  }

  public List<Stock> getStocksWithKijunCross() {
    LocalDate fromDate = LocalDate.now().minusMonths(1);
    LocalDate toDate = LocalDate.now();

    List<Stock> hits = Lists.newArrayList();

    for (Stock stock : stockDao.find()) {

      String symbol = stock.getSymbol();
      List<Value> tenkanList = meanDao.find(findSymbol(symbol).period(9).from(fromDate).to(toDate));
      List<Value> kijunList = meanDao.find(findSymbol(symbol).period(26).from(fromDate).to(toDate));
      List<Price> priceList = priceDao.find(findSymbol(symbol).from(fromDate).to(toDate));

      // Skip does w/ not enough document
      if (tenkanList.isEmpty() || priceList.isEmpty() || kijunList.isEmpty()) {
        continue;
      }

      // Get most recent document
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
    return 3; // need enough months of past document to calculate present ichimoku
  }
}
