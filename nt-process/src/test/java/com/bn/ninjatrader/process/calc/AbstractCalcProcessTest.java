package com.bn.ninjatrader.process.calc;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.process.request.CalcRequest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/30/16.
 */
public class AbstractCalcProcessTest {

  private final LocalDate yesterday = LocalDate.of(2016, 8, 1);
  private final LocalDate now = LocalDate.of(2016, 8, 2);
  private final LocalDate tomorrow = LocalDate.of(2016, 8, 3);

  private final Price price1 = Price.builder().date(yesterday).open(1).high(1).low(1).close(1).volume(1000).build();
  private final Price price2 = Price.builder().date(now).open(2).high(2).low(2).close(2).volume(2000).build();

  private PriceDao priceDao;
  private AbstractCalcProcess process;

  @Before
  public void before() {
    priceDao = mock(PriceDao.class);
    process = new DummyCalcProcess(priceDao);
  }

  @Test
  public void testGetFromDateToHaveEnoughBarsWithOneValue_shouldReturnDateOfOneBarAgo() {
    when(priceDao.findBeforeDate(any())).thenReturn(Lists.newArrayList(price2));

    assertThat(process.getFromDateToHaveEnoughBars(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(1).beforeDate(tomorrow).build()))
        .isEqualTo(now);
  }

  @Test
  public void testGetFromDateToHaveEnoughBarsWithTwoValues_shouldReturnTheOlderDate() {
    when(priceDao.findBeforeDate(any())).thenReturn(Lists.newArrayList(price1, price2));

    assertThat(process.getFromDateToHaveEnoughBars(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(2).beforeDate(tomorrow).build()))
        .isEqualTo(yesterday);
  }

  @Test
  public void testWithNumOfValuesGreaterThanAvailablePrices_shouldReturnRequestedDateBefore() {
    LocalDate fromDate = process.getFromDateToHaveEnoughBars(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).beforeDate(tomorrow).numOfValues(100).build());
    assertThat(fromDate).isEqualTo(tomorrow);
  }

  /**
   * Dummy class for testing.
   */
  private static final class DummyCalcProcess extends AbstractCalcProcess {
    public DummyCalcProcess(PriceDao priceDao) {
      super(priceDao);
    }
    @Override
    public void process(CalcRequest calcRequest) {}

    @Override
    public String getProcessName() {
      return null;
    }
  }
}
