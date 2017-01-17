package com.bn.ninjatrader.process.provider;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.AbstractValueDao;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class PriorValueProviderTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final LocalDate yesterday = now.minusDays(1);

  private AbstractValueDao valueDao;
  private PriorValueProvider priorValueProvider;

  @Before
  public void before() {
    valueDao = mock(AbstractValueDao.class);
    priorValueProvider = new PriorValueProvider();
  }

  @Test
  public void testProvidePriorValues_shouldRetrievePriorValueFromDao() {
    final ArgumentCaptor<FindBeforeDateRequest> requestCaptor = ArgumentCaptor.forClass(FindBeforeDateRequest.class);

    // Call provider
    priorValueProvider.providePriorValues(PriorValueRequest.builder()
        .dao(valueDao).symbol("MEG").timeFrame(TimeFrame.ONE_DAY).priorDate(now).addPeriods(10, 20).build());

    // Should retrieve from dao 2x, one for each period.
    verify(valueDao, times(2)).findBeforeDate(requestCaptor.capture());

    final FindBeforeDateRequest request1 = requestCaptor.getAllValues().get(0);
    final FindBeforeDateRequest request2 = requestCaptor.getAllValues().get(1);

    // Verify dao first request
    assertThat(request1.getBeforeDate()).isEqualTo(now);
    assertThat(request1.getNumOfValues()).isEqualTo(1);
    assertThat(request1.getPeriod()).isEqualTo(10);
    assertThat(request1.getSymbol()).isEqualTo("MEG");
    assertThat(request1.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);

    // Verify dao second request
    assertThat(request2.getBeforeDate()).isEqualTo(now);
    assertThat(request2.getNumOfValues()).isEqualTo(1);
    assertThat(request2.getPeriod()).isEqualTo(20);
    assertThat(request2.getSymbol()).isEqualTo("MEG");
    assertThat(request2.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);
  }

  @Test
  public void testProvidePriorValues_shouldReturnPriorValuesForEachPeriod() {
    when(valueDao.findBeforeDate(any(FindBeforeDateRequest.class))).thenReturn(
        Lists.newArrayList(Value.of(yesterday, 33.42)), // For period = 10
        Lists.newArrayList(Value.of(yesterday, 42.25))  // For period = 20
    );

    // Call provider
    final Map<Integer, Value> result = priorValueProvider.providePriorValues(PriorValueRequest.builder()
            .dao(valueDao).symbol("MEG").timeFrame(TimeFrame.ONE_DAY).priorDate(now).addPeriods(10, 20).build());

    // Verify returned result
    assertThat(result).hasSize(2);
    assertThat(result.get(10)).isEqualTo(Value.of(yesterday, 33.42));
    assertThat(result.get(20)).isEqualTo(Value.of(yesterday, 42.25));
  }
}
