package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.IchimokuDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.service.model.IchimokuResponse;
import com.google.common.collect.Lists;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class IchimokuResourceTest extends AbstractJerseyTest {

  private static final IchimokuDao ichimokuService = mock(IchimokuDao.class);
  private static final LocalDate date1 = LocalDate.of(2016, 2, 1);
  private static final LocalDate date2 = LocalDate.of(2016, 2, 5);
  private static final Ichimoku ichimoku1 = new Ichimoku(date1, 1, 2, 3, 4, 5);
  private static final Ichimoku ichimoku2 = new Ichimoku(date2, 5, 6, 7, 8, 9);
  private static final Clock fixedClock = TestUtil.fixedClock(date1);

  @Override
  protected ResourceConfig configureResource(final ResourceConfig resourceConfig) {
    return resourceConfig.register(new IchimokuResource(ichimokuService, fixedClock));
  }

  @Before
  public void before() {
    when(ichimokuService.find(any(FindRequest.class)))
        .thenReturn(Lists.newArrayList(ichimoku1, ichimoku2));
  }

  @After
  public void after() {
    reset(ichimokuService);
  }

  @Test
  public void requestIchimoku_shouldReturnList() {
    final IchimokuResponse response = target("/ichimoku/MEG")
        .queryParam("timeframe", "ONE_WEEK")
        .queryParam("from", "20160101")
        .queryParam("to", "20171231")
        .request().get(IchimokuResponse.class);

    assertThat(response.getValues()).containsExactly(ichimoku1, ichimoku2);
  }

  @Test
  public void requestIchimokuWithNoDataFound_shouldReturnEmptyList() {
    when(ichimokuService.find(any(FindRequest.class))).thenReturn(Collections.emptyList());

    final IchimokuResponse response = target("/ichimoku/MEG").request().get(IchimokuResponse.class);

    assertThat(response.getValues()).isNotNull();
    assertThat(response.getValues()).isEmpty();
  }
}
