package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.ValueCalculator;
import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.ValueDao;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bn.ninjatrader.model.request.FindRequest.forSymbol;
import static com.bn.ninjatrader.model.request.SaveRequest.save;

/**
 * Created by Brad on 7/28/16.
 */
@Singleton
public abstract class AbstractCalcValuesProcess extends AbstractCalcProcess implements CalcProcess {

  private static final Logger log = LoggerFactory.getLogger(AbstractCalcValuesProcess.class);

  private final ValueCalculator calculator;
  private final PriceDao priceDao;
  private final ValueDao valueDao;

  @Inject
  public AbstractCalcValuesProcess(ValueCalculator calculator, PriceDao priceDao, ValueDao valueDao) {
    super(priceDao);
    this.priceDao = priceDao;
    this.calculator = calculator;
    this.valueDao = valueDao;
  }

  @Override
  public void processMissingBars(CalcRequest calcRequest) {
    Preconditions.checkNotNull(calcRequest);
    log.debug("{}", calcRequest);

    LocalDate priceFromDate = getFromDateToHaveEnoughPricesForMissingBars(calcRequest);
    int[] periods = getDefaultPeriodsIfNull(calcRequest);

    List<Price> prices = priceDao.find(forSymbol(calcRequest.getSymbol())
        .from(priceFromDate)
        .to(calcRequest.getToDate()));

    Map<Integer, List<Value>> periodToValuesMap = calculator.calc(CalcParams.withPrice(prices).periods(periods));

    saveValuesForEachPeriod(calcRequest, periodToValuesMap);
  }

  private LocalDate getFromDateToHaveEnoughPricesForMissingBars(CalcRequest calcRequest) {
    int[] periods = getDefaultPeriodsIfNull(calcRequest);
    int biggestPeriod = NumUtil.max(periods);
    return getFromDateToHaveEnoughBars(calcRequest, biggestPeriod);
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
        valueDao.save(save(calcRequest.getSymbol()).period(period).values(values));
      }
    }
  }

  protected abstract int[] getDefaultPeriods();
}
