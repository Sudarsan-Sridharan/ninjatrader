package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.MeanDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.service.model.MultiPeriodResponse;
import com.google.common.collect.Lists;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
public class MeanResourceTest extends AbstractJerseyTest {

  private static final MeanDao meanDao = mock(MeanDao.class);
  private static final LocalDate date1 = LocalDate.of(2016, 2, 1);
  private static final LocalDate date2 = LocalDate.of(2016, 2, 5);
  private static final Value value1 = Value.of(date1, 1);
  private static final Value value2 = Value.of(date2, 5);
  private static final Clock fixedClock = TestUtil.fixedClock(date1);

  @Override
  protected ResourceConfig configureResource(final ResourceConfig resourceConfig) {
    return resourceConfig.register(new MeanResource(meanDao, fixedClock));
  }

  @Before
  public void before() {
    when(meanDao.find(any(FindRequest.class)))
        .thenReturn(Lists.newArrayList(value1, value2));
  }

  @After
  public void after() {
    Mockito.reset(meanDao);
  }

  @Test
  public void requestValuesForOnePeriod_shouldReturnValuesForOnePeriod() {
    final int period = 9;

    final MultiPeriodResponse<Value> response = target("/mean/MEG")
        .queryParam("timeframe", "ONE_DAY")
        .queryParam("from", "20160101")
        .queryParam("to", "20171231")
        .queryParam("period", period)
        .request()
        .get(MultiPeriodResponse.class);

    // has only 1 period entry.
    assertThat(response.getValues().keySet()).hasSize(1);

    // has 2 values for that period.
    assertThat(response.getValues().get(period)).hasSize(2);

    final List<Value> values = Lists.newArrayList(response.getValues().get(period));
    assertThat(values).containsExactly(value1, value2);
  }

  @Test
  public void requestValuesForMultiPeriods_shouldReturnValuesForEachPeriod() {
    final int period1 = 20;
    final int period2 = 50;
    final int period3 = 100;
    final int unknownPeriod = 200;

    final MultiPeriodResponse response = target(String.format("/mean/MEG"))
        .queryParam("period", period1)
        .queryParam("period", period2)
        .queryParam("period", period3)
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
