package com.bn.ninjatrader.process.provider;

import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.AbstractValueDao;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriorValueProvider {
  private static final Logger LOG = LoggerFactory.getLogger(PriorValueProvider.class);

  public <T extends Value> CalcParams<T> provideContinuedValues(final AbstractValueDao dao,
                                                final String symbol,
                                                final TimeFrame timeFrame,
                                                final List<Price> prices,
                                                final int ... periods) {
    checkNotNull(prices, "prices must not be null.");
    checkArgument(!prices.isEmpty(), "prices must not be empty.");

    try {
      final Price price = prices.get(0);
      final CalcParams calcParams = CalcParams.withPrice(prices).periods(periods);

      // For each period, get the previous value to continue calculating from.
      for (final int period : periods) {
        final List<T> values = dao.findBeforeDate(FindBeforeDateRequest.builder()
            .symbol(symbol).timeFrame(timeFrame).numOfValues(1).beforeDate(price.getDate()).period(period).build());
        if (!values.isEmpty()) {
          calcParams.addContinueFromValue(period, values.get(0));
        }
      }
      return calcParams;
    } catch (Exception e) {
      LOG.error("Error providing CalcParams for: {} {}", symbol, timeFrame);
      throw e;
    }
  }
}
