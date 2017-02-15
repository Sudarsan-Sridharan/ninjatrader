package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.PriceChangeCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.bn.ninjatrader.common.type.TimeFrame.ONE_DAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    when(priceDao.find(any())).thenReturn(priceList);
    when(calculator.calc(priceList)).thenReturn(processedPriceList);
  }

  @Test
  public void testProcessOneSymbol_shouldSaveProcessedValuesForThatSymbol() {
    final ArgumentCaptor<SaveRequest> saveRequestCaptor = ArgumentCaptor.forClass(SaveRequest.class);

    process.process(CalcRequest.forSymbol("MEG").timeFrames(ONE_DAY));

    verify(priceDao).save(saveRequestCaptor.capture());

    assertThat(saveRequestCaptor.getValue())
        .isEqualTo(SaveRequest.save("MEG").timeFrame(ONE_DAY).values(processedPriceList));
  }

  @Test
  public void testProcessMultipleSymbolsAndTimeFrames_shouldProcessEachSymbolAndTimeFrame() {
    final ArgumentCaptor<SaveRequest> saveRequestCaptor = ArgumentCaptor.forClass(SaveRequest.class);

    process.process(CalcRequest.forSymbols("MEG", "BDO", "MBT").allTimeFrames());

    // Should save 3 symbols * each time frame.
    verify(priceDao, times(3 * TimeFrame.values().length)).save(saveRequestCaptor.capture());

    // To list of time frames per symbol.
    final Multimap<String, TimeFrame> map = HashMultimap.create();
    final List<SaveRequest> saveRequests = saveRequestCaptor.getAllValues();
    for (SaveRequest saveRequest : saveRequests) {
      map.put(saveRequest.getSymbol(), saveRequest.getTimeFrame());
    }

    assertThat(map.keys()).containsOnly("MEG", "BDO", "MBT");
    assertThat(map.get("MEG")).containsOnly(TimeFrame.values());
    assertThat(map.get("BDO")).containsOnly(TimeFrame.values());
    assertThat(map.get("MBT")).containsOnly(TimeFrame.values());
  }
}
