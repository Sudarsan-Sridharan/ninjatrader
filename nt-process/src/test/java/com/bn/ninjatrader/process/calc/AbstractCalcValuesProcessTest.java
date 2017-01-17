package com.bn.ninjatrader.process.calc;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.bn.ninjatrader.calculator.ValueCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.ValueDao;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.bn.ninjatrader.process.request.CalcRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.bn.ninjatrader.process.request.CalcRequest.calcSymbol;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Brad on 8/16/16.
 */
public class AbstractCalcValuesProcessTest {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractCalcValuesProcessTest.class);

  private final LocalDate DATE_2014 = LocalDate.of(2014, 1, 1);
  private final LocalDate DATE_2015 = LocalDate.of(2015, 1, 1);
  private final LocalDate DATE_2017 = LocalDate.of(2017, 1, 1);
  private final List<Price> priceList = TestUtil.randomPrices(5);
  private final Map<Integer, List<Value>> calcResult = Maps.newHashMap();

  private ValueCalculator calculator;
  private PriceDao priceDao;
  private ValueDao valueDao;
  private AbstractCalcValuesProcess process;

  @Before
  public void setup() {
    calculator = mock(ValueCalculator.class);
    priceDao = mock(PriceDao.class);
    valueDao = mock(ValueDao.class);

    process = new DummyProcess(calculator, priceDao, valueDao);

    calcResult.clear();

    when(priceDao.find(any())).thenReturn(priceList);
  }

  @Test
  public void testProcessWithRequestDatesOutOfRange_shouldNotSave() {
    LocalDate date = LocalDate.MIN; // out of range
    when(calculator.calc(any())).thenReturn(calcResult);

    process.process(calcSymbol("MEG").from(date).to(date).periods(process.getDefaultPeriods()));

    verify(priceDao, times(0)).save(any(SaveRequest.class));
  }

  @Test
  public void testProcessWithNoValueData_shouldNotSave() {
    calcResult.put(20, Collections.emptyList());
    when(calculator.calc(any())).thenReturn(calcResult);

    process.process(calcSymbol("MEG").from(DATE_2014).to(DATE_2017).periods(20));

    verify(priceDao, times(0)).save(any(SaveRequest.class));
  }

  @Test
  public void testProcessWithMultipleSymbols_shouldSaveForEachSymbol() {
    ArgumentCaptor<SaveRequest> saveRequestCaptor = ArgumentCaptor.forClass(SaveRequest.class);
    calcResult.put(20, TestUtil.randomValues(3));
    when(calculator.calc(any())).thenReturn(calcResult);

    process.process(CalcRequest.calcSymbol("MEG", "BDO", "TEL")
        .timeFrames(TimeFrame.ONE_DAY)
        .from(DATE_2014)
        .to(DATE_2017));

    verify(valueDao, times(3)).save(saveRequestCaptor.capture());
    assertThat(saveRequestCaptor.getAllValues()).extracting("symbol").containsOnly("MEG", "BDO", "TEL");
  }

  @Test
  public void testProcessWithMultiplePeriods_shouldSaveForEachPeriod() {
    ArgumentCaptor<SaveRequest> saveRequestCaptor = ArgumentCaptor.forClass(SaveRequest.class);
    calcResult.put(20, TestUtil.randomValues(3));
    calcResult.put(50, TestUtil.randomValues(3));
    calcResult.put(100, TestUtil.randomValues(2));
    when(calculator.calc(any())).thenReturn(calcResult);

    process.process(CalcRequest.calcSymbol("MEG")
        .timeFrames(TimeFrame.ONE_DAY)
        .from(DATE_2014)
        .to(DATE_2017)
        .periods(20, 50, 100));

    verify(valueDao, times(3)).save(saveRequestCaptor.capture());
    assertThat(saveRequestCaptor.getAllValues()).extracting("period").containsOnly(20, 50, 100);
  }

  @Test
  public void testProcessWithMultipleTimeFrames_shouldSaveForEachTimeFrame() {
    ArgumentCaptor<SaveRequest> saveRequestCaptor = ArgumentCaptor.forClass(SaveRequest.class);
    calcResult.clear();
    calcResult.put(20, TestUtil.randomValues(3));
    when(calculator.calc(any())).thenReturn(calcResult);

    process.process(CalcRequest.calcSymbol("MEG")
        .allTimeFrames()
        .from(DATE_2014)
        .to(DATE_2017));

    verify(valueDao, times(2)).save(saveRequestCaptor.capture());

    List<SaveRequest> saveRequests = saveRequestCaptor.getAllValues();
    assertThat(saveRequests).extracting("timeFrame").containsOnly(TimeFrame.values());
  }

  @Test
  public void testProcessWithMultipleSymbolsTimeFramesPeriods_shouldSaveForEachSymbolTimeFramePeriod() {
    ArgumentCaptor<SaveRequest> saveRequestCaptor = ArgumentCaptor.forClass(SaveRequest.class);
    calcResult.clear();
    calcResult.put(20, TestUtil.randomValues(3));
    calcResult.put(50, TestUtil.randomValues(3));
    calcResult.put(100, TestUtil.randomValues(3));
    when(calculator.calc(any())).thenReturn(calcResult);

    process.process(CalcRequest.calcSymbol("MEG", "BDO")
        .allTimeFrames()
        .from(DATE_2014)
        .to(DATE_2017));

    // Should save 2 symbols * 3 periods * time frames
    verify(valueDao, times(2 * 3 * TimeFrame.values().length)).save(saveRequestCaptor.capture());

    List<SaveRequest> saveRequests = saveRequestCaptor.getAllValues();
    assertThat(saveRequests).extracting("timeFrame").containsOnly(TimeFrame.values());
    assertThat(saveRequests).extracting("symbol").containsOnly("MEG", "BDO");
    assertThat(saveRequests).extracting("period").containsOnly(20, 50, 100);
  }

  @Test
  public void testProcessWithDateValuesOutOfRequestRange_shouldSaveOnlyWithinRequestRange() {
    ArgumentCaptor<SaveRequest> saveRequestCaptor = ArgumentCaptor.forClass(SaveRequest.class);
    calcResult.clear();
    calcResult.put(20, TestUtil.randomValuesForDateRange(DATE_2014.plusMonths(6), DATE_2015));
    when(calculator.calc(any())).thenReturn(calcResult);

    process.process(CalcRequest.calcSymbol("MEG")
        .timeFrames(TimeFrame.ONE_DAY)
        .from(DATE_2015)
        .to(DATE_2015));

    verify(valueDao).save(saveRequestCaptor.capture());

    SaveRequest saveRequest = saveRequestCaptor.getValue();
    assertThat(saveRequest.getValues()).hasSize(1).extracting("date").containsExactly(DATE_2015);
  }

  /**
   * Dummy class extending abstract class for test purposes.
   */
  private static final class DummyProcess extends AbstractCalcValuesProcess {
    public DummyProcess(final ValueCalculator calculator,
                        final PriceDao priceDao,
                        final ValueDao valueDao) {
      super(calculator, priceDao, valueDao);
    }
    @Override
    protected List<Integer> getDefaultPeriods() {
      return Lists.newArrayList(20, 50, 100);
    }
    @Override
    public String getProcessName() {
      return "DummyProcess";
    }
  }
}
