package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.ValueCalculator;
import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.ValueDao;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;
import static com.bn.ninjatrader.model.request.SaveRequest.save;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 7/28/16.
 */
@Singleton
public abstract class AbstractCalcValuesProcess extends AbstractCalcProcess implements CalcProcess {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractCalcValuesProcess.class);

  private final ValueCalculator calculator;
  private final PriceDao priceDao;
  private final ValueDao valueDao;

  @Inject
  public AbstractCalcValuesProcess(final ValueCalculator calculator,
                                   final PriceDao priceDao,
                                   final ValueDao valueDao) {
    super(priceDao);
    this.priceDao = priceDao;
    this.calculator = calculator;
    this.valueDao = valueDao;
  }

  @Override
  public void process(final CalcRequest calcRequest) {
    checkNotNull(calcRequest);

    for (final String symbol : calcRequest.getAllSymbols()) {
      for (final TimeFrame timeFrame : calcRequest.getTimeFrames()) {

        final List<Integer> periods = calcRequest.getPeriods().isEmpty() ? getDefaultPeriods() : calcRequest.getPeriods();
        final int biggestPeriod = NumUtil.max(periods);
        final LocalDate priceFromDate = getFromDateToHaveEnoughBars(FindBeforeDateRequest.builder()
            .symbol(symbol)
            .timeFrame(timeFrame)
            .beforeDate(calcRequest.getFromDate())
            .numOfValues(biggestPeriod)
            .build());

        final List<Price> prices = priceDao.find(findSymbol(symbol)
            .timeframe(timeFrame)
            .from(priceFromDate)
            .to(calcRequest.getToDate()));

        if (!prices.isEmpty()) {
          final Map<Integer, List<Value>> periodToValuesMap =
              calculator.calc(provideCalcParams(symbol, timeFrame, prices, periods));
          saveValuesForEachPeriod(symbol, timeFrame, calcRequest.getFromDate(), calcRequest.getToDate(), periodToValuesMap);
        }
      }
    }
  }

  public CalcParams provideCalcParams(String symbol, TimeFrame timeFrame, List<Price> prices, List<Integer> periods) {
    return CalcParams.withPrices(prices).periods(periods);
  }

  private void saveValuesForEachPeriod(final String symbol,
                                       final TimeFrame timeFrame,
                                       final LocalDate fromDate,
                                       final LocalDate toDate,
                                       final Map<Integer, List<Value>> periodToValuesMap) {
    // For each period, save list of values
    for (final Map.Entry<Integer, List<Value>> entry : periodToValuesMap.entrySet()) {
      final int period = entry.getKey().intValue();
      final List<Value> values = entry.getValue();
      DateObjUtil.trimToDateRange(values, fromDate, toDate);

      if (!values.isEmpty()) {
        valueDao.save(save(symbol).timeFrame(timeFrame).period(period).values(values));
      }
    }
  }

  protected abstract List<Integer> getDefaultPeriods();
}
