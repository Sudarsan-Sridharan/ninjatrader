package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.RSIValue;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.RSIDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.service.model.MultiPeriodResponse;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.google.common.collect.Lists;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.GenericType;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class RSIResourceTest {

  private static final RSIDao rsiDao = mock(RSIDao.class);
  private static final LocalDate date1 = LocalDate.of(2016, 2, 1);
  private static final LocalDate date2 = LocalDate.of(2016, 2, 5);
  private static final RSIValue value1 = RSIValue.of(date1, 1);
  private static final RSIValue value2 = RSIValue.of(date2, 5);
  private static final Clock fixedClock = TestUtil.fixedClock(date1);

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new RSIResource(rsiDao, fixedClock))
      .addProvider(LocalDateParamConverterProvider.class)
      .build();

  @Before
  public void before() {
    when(rsiDao.find(any(FindRequest.class)))
        .thenReturn(Lists.newArrayList(value1, value2));
  }

  @After
  public void after() {
    Mockito.reset(rsiDao);
  }

  @Test
  public void requestValuesForOnePeriod_shouldReturnValuesForOnePeriod() {
    int period = 20;

    MultiPeriodResponse<RSIValue> response = resources.client()
        .target(String.format("/rsi/MEG?timeframe=ONE_DAY&from=20160101&to=20171231&period=%d", period))
        .request()
        .get(new GenericType<MultiPeriodResponse<RSIValue>>(){});

    // has only 1 period entry.
    assertThat(response.getValues().keySet()).hasSize(1);

    // has 2 values for that period.
    assertThat(response.getValues().get(period)).hasSize(2);

    List<RSIValue> values = Lists.newArrayList(response.getValues().get(period));
    assertThat(values).hasSize(2);

    assertThat(values.get(0)).isEqualTo(value1);
    assertThat(values.get(1)).isEqualTo(value2);
  }

  @Test
  public void requestValuesForMultiPeriods_shouldReturnValuesForEachPeriod() {
    int period1 = 20;
    int period2 = 50;
    int period3 = 100;
    int unknownPeriod = 200;

    MultiPeriodResponse<RSIValue> response = resources.client()
        .target(String.format("/rsi/MEG?period=%d&period=%d&period=%d", period1, period2, period3))
        .request()
        .get(MultiPeriodResponse.class);

    // Has 3 entries, 1 for each period.
    assertThat(response.getValues().keySet()).hasSize(3);

    // Each period has 2 values.
    assertThat(response.getValues().get(period1)).hasSize(2);
    assertThat(response.getValues().get(period2)).hasSize(2);
    assertThat(response.getValues().get(period3)).hasSize(2);

    assertThat(response.getValues().get(unknownPeriod)).isEmpty();
  }
}
