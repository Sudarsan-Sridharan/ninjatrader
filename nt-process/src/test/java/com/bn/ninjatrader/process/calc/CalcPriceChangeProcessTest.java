package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.PriceChangeCalculator;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.process.request.CalcRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.bn.ninjatrader.common.type.TimeFrame.ONE_DAY;
import static com.bn.ninjatrader.common.type.TimeFrame.ONE_WEEK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Brad on 8/30/16.
 */
public class CalcPriceChangeProcessTest {
  private static final Logger LOG = LoggerFactory.getLogger(CalcPriceChangeProcessTest.class);

  private final List<Price> priceList = TestUtil.randomPrices(3);
  private final List<Price> processedPriceList = TestUtil.randomPrices(3);

  private PriceChangeCalculator calculator;
  private PriceDao priceDao;
  private CalcPriceChangeProcess process;

  @Before
  public void setup() {
    calculator = mock(PriceChangeCalculator.class);
    priceDao = mock(PriceDao.class);
    process = new CalcPriceChangeProcess(calculator, priceDao);

    when(priceDao.savePrices(any())).thenReturn(mock(PriceDao.SavePricesOperation.class, RETURNS_SELF));
    when(priceDao.findPrices()).thenReturn(mock(PriceDao.FindPricesOperation.class, RETURNS_SELF));
    when(priceDao.findPrices().now()).thenReturn(priceList);
    when(calculator.calc(priceList)).thenReturn(processedPriceList);
  }

  @Test
  public void testProcessOneSymbol_shouldSaveProcessedValuesForThatSymbol() {
    process.process(CalcRequest.forSymbol("MEG").timeFrames(ONE_DAY));

    verify(priceDao).savePrices(processedPriceList);
    verify(priceDao.savePrices(any())).withSymbol("MEG");
    verify(priceDao.savePrices(any())).withTimeFrame(ONE_DAY);
    verify(priceDao.savePrices(any())).now();
  }

  @Test
  public void testProcessMultipleSymbolsAndTimeFrames_shouldProcessEachSymbolAndTimeFrame() {
    final ArgumentCaptor<TimeFrame> timeFrameCaptor = ArgumentCaptor.forClass(TimeFrame.class);

    process.process(CalcRequest.forSymbols("MEG", "BDO", "MBT").allTimeFrames());

    // Should save 3 symbols * each time frame.
    final int times = 3 * TimeFrame.values().length;
    verify(priceDao, times(times)).savePrices(any());

    verify(priceDao.savePrices(any()), times(2)).withSymbol("MEG");
    verify(priceDao.savePrices(any()), times(2)).withSymbol("BDO");
    verify(priceDao.savePrices(any()), times(2)).withSymbol("MBT");
    verify(priceDao.savePrices(any()), times(3)).withTimeFrame(timeFrameCaptor.capture());
    verify(priceDao.savePrices(any()), times(times)).now();

    assertThat(timeFrameCaptor.getAllValues()).containsExactly(ONE_WEEK, ONE_DAY, ONE_WEEK, ONE_DAY, ONE_WEEK, ONE_DAY);
  }
}
