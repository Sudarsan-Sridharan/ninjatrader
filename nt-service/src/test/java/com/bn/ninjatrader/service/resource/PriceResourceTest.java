package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.service.model.PriceResponse;
import com.google.common.collect.Lists;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.ws.rs.NotFoundException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceResourceTest extends AbstractJerseyTest {

  private static final PriceDao priceDao = mock(PriceDao.class);
  private static final LocalDate date1 = LocalDate.of(2016, 2, 1);
  private static final LocalDate date2 = LocalDate.of(2016, 2, 5);
  private static final Price price1 = new Price(date1, 1, 2, 3, 4, 10000);
  private static final Price price2 = new Price(date2, 5, 6, 7, 8, 20000);
  private static final Clock fixedClock = TestUtil.fixedClock(date1);

  @Override
  protected ResourceConfig configureResource(final ResourceConfig resourceConfig) {
    return resourceConfig.register(new PriceResource(priceDao, fixedClock));
  }

  @Before
  public void before() {
    when(priceDao.find(any(FindRequest.class)))
        .thenReturn(Lists.newArrayList(price1, price2));
  }

  @After
  public void after() {
    Mockito.reset(priceDao);
  }

  @Test
  public void testPriceResponse() {
    final PriceResponse priceResponse = target("/price/MEG")
        .queryParam("timeframe", "ONE_WEEK")
        .queryParam("from", "20160101")
        .queryParam("to", "20171231")
        .request()
        .get(PriceResponse.class);

    assertThat(priceResponse.getPriceList()).containsExactly(price1, price2);
    assertThat(priceResponse.getFromDate()).isEqualTo(date1);
    assertThat(priceResponse.getToDate()).isEqualTo(date2);
  }

  @Test
  public void testQueryParams_shouldParseQueryParams() {
    target("/price/BDO")
        .queryParam("timeframe", "ONE_WEEK")
        .queryParam("from", "20160101")
        .queryParam("to", "20171231")
        .request()
        .get(PriceResponse.class);

    final ArgumentCaptor<FindRequest> captor = ArgumentCaptor.forClass(FindRequest.class);
    verify(priceDao, times(1)).find(captor.capture());

    final FindRequest findRequest = captor.getValue();
    assertThat(findRequest.getSymbol()).isEqualTo("BDO");
    assertThat(findRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_WEEK);
    assertThat(findRequest.getFromDate()).isEqualTo(LocalDate.of(2016, 1, 1));
    assertThat(findRequest.getToDate()).isEqualTo(LocalDate.of(2017, 12, 31));
  }

  @Test
  public void testDefaultQueryParams() {
    target("/price/MBT").request().get(PriceResponse.class);

    final ArgumentCaptor<FindRequest> captor = ArgumentCaptor.forClass(FindRequest.class);
    verify(priceDao, times(1)).find(captor.capture());

    final FindRequest findRequest = captor.getValue();
    assertThat(findRequest.getSymbol()).isEqualTo("MBT");
    assertThat(findRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);
    assertThat(findRequest.getFromDate()).isEqualTo(LocalDate.now(fixedClock).minusYears(2));
    assertThat(findRequest.getToDate()).isEqualTo(LocalDate.now(fixedClock));
  }

  @Test
  public void testWithNoPricesFound() {
    when(priceDao.find(any(FindRequest.class)))
        .thenReturn(Collections.emptyList());

    final PriceResponse priceResponse = target("/price/MBT").request().get(PriceResponse.class);

    assertThat(priceResponse.getFromDate()).isNull();
    assertThat(priceResponse.getToDate()).isNull();
    assertThat(priceResponse.getPriceList()).isEmpty();
  }

  @Test(expected = NotFoundException.class)
  public void testInvalidPath() {
    target("/price").request().get(PriceResponse.class);
  }
}
