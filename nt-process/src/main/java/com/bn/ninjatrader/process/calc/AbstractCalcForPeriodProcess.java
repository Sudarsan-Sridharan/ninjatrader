package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.CalculatorForPeriod;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bn.ninjatrader.model.dao.period.FindRequest.forSymbol;

/**
 * Created by Brad on 7/28/16.
 */
@Singleton
public abstract class AbstractCalcForPeriodProcess implements CalcProcess {

  private static final Logger log = LoggerFactory.getLogger(CalcMeanProcess.class);

  @Inject
  private PriceDao priceDao;

  private final CalculatorForPeriod calculator;

  @Inject
  public AbstractCalcForPeriodProcess(CalculatorForPeriod calculator) {
    this.calculator = calculator;
  }

  @Override
  public void processPrices(CalcRequest calcRequest) {
    Preconditions.checkNotNull(calcRequest);
    log.debug("{}", calcRequest);

    String symbol = calcRequest.getSymbol();
    LocalDate fromDate = calcRequest.getFromDate();
    LocalDate toDate = calcRequest.getToDate();
    int[] periods = getDefaultPeriodsIfNull(calcRequest);

    List<Price> prices = priceDao.find(forSymbol(symbol).from(fromDate).to(toDate));

    Map<Integer, List<Value>> periodToValuesMap = calculator.calc(prices, periods);

    saveValuesForEachPeriod(calcRequest, periodToValuesMap);
  }

  @Override
  public void processMissingBars(CalcRequest calcRequest) {
    Preconditions.checkNotNull(calcRequest);
    log.debug("{}", calcRequest);

    String symbol = calcRequest.getSymbol();
    LocalDate priceFromDate = getFromDateToHaveEnoughPricesForMissingBars(calcRequest);
    LocalDate priceToDate = calcRequest.getToDate();
    int[] periods = getDefaultPeriodsIfNull(calcRequest);

    List<Price> prices = priceDao.find(forSymbol(symbol).from(priceFromDate).to(priceToDate));

    Map<Integer, List<Value>> periodToValuesMap = calculator.calc(prices, periods);

    saveValuesForEachPeriod(calcRequest, periodToValuesMap);
  }

  private LocalDate getFromDateToHaveEnoughPricesForMissingBars(CalcRequest calcRequest) {
    String symbol = calcRequest.getSymbol();
    LocalDate fromDate = calcRequest.getFromDate();
    int[] periods = getDefaultPeriodsIfNull(calcRequest);
    int biggestPeriod = NumUtil.max(periods);

    List<Price> prices = priceDao.findNBarsBeforeDate(symbol, biggestPeriod, fromDate);
    if (!prices.isEmpty()) {
      return prices.get(0).getDate();
    }
    return calcRequest.getFromDate();
  }

  private int[] getDefaultPeriodsIfNull(CalcRequest calcRequest) {
    int[] periods = calcRequest.getPeriods();
    return periods == null ? getDefaultPeriods() : periods;
  }

  private void saveValuesForEachPeriod(CalcRequest calcRequest, Map<Integer, List<Value>> periodToValuesMap) {
    for (Map.Entry<Integer, List<Value>> entry : periodToValuesMap.entrySet()) {
      int period = entry.getKey().intValue();
      List<Value> values = entry.getValue();

      DateObjUtil.trimToDateRange(values, calcRequest.getFromDate(), calcRequest.getToDate());

      if (!values.isEmpty()) {
        save(calcRequest.getSymbol(), period, values);
      }
    }
  }

  protected abstract int[] getDefaultPeriods();

  protected abstract void save(String symbol, int period, List<Value> values);
}
