package com.bn.ninjatrader.process.calc;

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
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

  @BeforeMethod
  public void setup() {
    calculator = mock(ValueCalculator.class);
    priceDao = mock(PriceDao.class);
    valueDao = mock(ValueDao.class);

    process = new DummyProcess(calculator, priceDao, valueDao);

    calcResult.clear();
    calcResult.put(1, Collections.emptyList());
    calcResult.put(20, TestUtil.randomValues(3));
    calcResult.put(50, TestUtil.randomValues(3));
    calcResult.put(100, TestUtil.randomValues(2));

    when(priceDao.find(any())).thenReturn(priceList);
  }

  @Test
  public void testProcessWithNoData_shouldNotSave() {
    LocalDate date = LocalDate.MIN;
    process.process(calcSymbol("MEG").from(date).to(date).periods(process.getDefaultPeriods()));

    verify(priceDao, times(0)).save(any(SaveRequest.class));
  }

  @Test
  public void TestProcessWithMultiplePeriods_shouldSaveForEachPeriod() {
    ArgumentCaptor<SaveRequest> saveRequestCaptor = ArgumentCaptor.forClass(SaveRequest.class);
    when(calculator.calc(any())).thenReturn(calcResult);

    process.process(CalcRequest.calcSymbol("MEG")
        .timeFrames(TimeFrame.ONE_DAY)
        .from(DATE_2014)
        .to(DATE_2017)
        .periods(new int[] {20, 50, 100}));

    verify(valueDao, times(3)).save(saveRequestCaptor.capture());

    List<SaveRequest> saveRequests = saveRequestCaptor.getAllValues();
    assertThat(saveRequests).extracting("period").containsOnly(20, 50, 100);
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
    protected int[] getDefaultPeriods() {
      return new int[] { 20, 50, 100};
    }

    @Override
    public String getProcessName() {
      return "DummyProcess";
    }
  }
}
