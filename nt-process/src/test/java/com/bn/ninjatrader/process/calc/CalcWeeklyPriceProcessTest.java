package com.bn.ninjatrader.process.calc;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.calculator.WeeklyPriceCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Collections;

import static com.bn.ninjatrader.process.request.CalcRequest.calcSymbol;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Brad on 8/16/16.
 */
public class CalcWeeklyPriceProcessTest {

  private final LocalDate week1 = LocalDate.of(2016, 2, 1);
  private final LocalDate week2 = LocalDate.of(2016, 2, 8);
  private final LocalDate week3 = LocalDate.of(2016, 2, 15);

  private final Price weeklyPrice1 = Price.builder().date(week1).open(1.1).high(1.2).low(1.3).close(1.4).volume(1000).build();
  private final Price weeklyPrice2 = Price.builder().date(week2).open(2.1).high(2.2).low(2.3).close(2.4).volume(2000).build();
  private final Price weeklyPrice3 = Price.builder().date(week3).open(3.1).high(3.2).low(3.3).close(3.4).volume(3000).build();

  private WeeklyPriceCalculator calculator;
  private PriceDao priceDao;
  private CalcWeeklyPriceProcess process;

  @Before
  public void setup() {
    calculator = mock(WeeklyPriceCalculator.class);
    priceDao = mock(PriceDao.class);
    process = new CalcWeeklyPriceProcess(calculator, priceDao);

    when(priceDao.find(any())).thenReturn(Collections.emptyList());
    when(calculator.calc(any())).thenReturn(Lists.newArrayList(weeklyPrice1, weeklyPrice2, weeklyPrice3));
  }

  @Test
  public void testProcessBars_shouldAdjustFromDateToStartOfWeek() {
    final ArgumentCaptor<FindRequest> findRequestCaptor = ArgumentCaptor.forClass(FindRequest.class);
    final LocalDate fromDate = LocalDate.of(2016, 2, 5);
    final LocalDate toDate = LocalDate.of(2016, 2, 10);

    process.process(calcSymbol("MEG").from(fromDate).to(toDate));

    verify(priceDao).find(findRequestCaptor.capture());
    final FindRequest findRequest = findRequestCaptor.getValue();
    assertThat(findRequest.getSymbol()).isEqualTo("MEG");
    assertThat(findRequest.getFromDate()).isEqualTo(LocalDate.of(2016, 2, 1));
    assertThat(findRequest.getToDate()).isEqualTo(LocalDate.of(2016, 2, 10));
  }

  @Test
  public void testSaveOnlyValuesWithinRequestedDates() {
    final ArgumentCaptor<SaveRequest> saveRequestCaptor = ArgumentCaptor.forClass(SaveRequest.class);
    final LocalDate fromDate = LocalDate.of(2016, 2, 5);
    final LocalDate toDate = LocalDate.of(2016, 2, 10);

    process.process(calcSymbol("MEG").from(fromDate).to(toDate));

    verify(priceDao).save(saveRequestCaptor.capture());
    final SaveRequest saveRequest = saveRequestCaptor.getValue();
    assertThat(saveRequest.getValues()).containsExactly(weeklyPrice2);
    assertThat(saveRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_WEEK);
  }
}
