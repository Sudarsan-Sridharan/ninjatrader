package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.calculator.IchimokuCalculator;
import com.bn.ninjatrader.calculator.parameter.IchimokuParameters;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class IchimokuDao {

  private static final Logger LOG = LoggerFactory.getLogger(IchimokuDao.class);

  private PriceDao priceDao;

  private MeanDao meanDao;

  @Inject
  private IchimokuCalculator ichimokuCalculator;

  @Inject
  public IchimokuDao(PriceDao priceDao, MeanDao meanDao) {
    this.priceDao = priceDao;
    this.meanDao = meanDao;
  }

  public void fillFutureDates(final Collection<Ichimoku> ichimokuList) {
    LocalDate lastDate = LocalDate.now();
    for (final Ichimoku ichimoku : ichimokuList) {
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
    TimeFrame timeFrame = findRequest.getTimeFrame();

    int numOfPastMonths = getNumOfPastMonths();
    LocalDate findMeanFromDate = fromDate.minusMonths(numOfPastMonths);

    final List<Price> prices = priceDao.find(findSymbol(symbol)
        .from(findMeanFromDate).to(toDate).timeframe(timeFrame));
    final List<Value> tenkanList = meanDao.find(findSymbol(symbol)
        .period(9).from(findMeanFromDate).to(toDate).timeframe(timeFrame));
    final List<Value> kijunList = meanDao.find(findSymbol(symbol)
        .period(26).from(findMeanFromDate).to(toDate).timeframe(timeFrame));
    final List<Value> senKouBList = meanDao.find(findSymbol(symbol)
        .period(52).from(findMeanFromDate).to(toDate).timeframe(timeFrame));

    final List<Ichimoku> ichimokuList = ichimokuCalculator.calc(
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

  protected int getNumOfPastMonths() {
    return 3; // need enough months of past document to calculate present ichimoku
  }
}
