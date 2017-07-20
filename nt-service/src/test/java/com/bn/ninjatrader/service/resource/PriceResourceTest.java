package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.service.model.PriceResponse;
import com.google.common.collect.Lists;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.NotFoundException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceResourceTest extends AbstractJerseyTest {

  private static final PriceDao priceDao = mock(PriceDao.class);
  private static final LocalDate date1 = LocalDate.of(2016, 2, 1);
  private static final LocalDate date2 = LocalDate.of(2016, 2, 5);
  private static final Price price1 = Price.builder()
      .date(date1).open(1).high(2).low(3).close(4).volume(10000).build();
  private static final Price price2 = Price.builder()
      .date(date2).open(5).high(6).low(7).close(8).volume(20000).build();
  private static final Clock clock = TestUtil.fixedClock(date1);

  @Override
  protected ResourceConfig configureResource(final ResourceConfig resourceConfig) {
    return resourceConfig.register(new PriceResource(priceDao, clock));
  }

  @Before
  public void before() {
    reset(priceDao);

    when(priceDao.findPrices()).thenReturn(mock(PriceDao.FindPricesOperation.class, RETURNS_SELF));
    when(priceDao.findPrices().now()).thenReturn(Lists.newArrayList(price1, price2));
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

    verify(priceDao.findPrices()).withSymbol("BDO");
    verify(priceDao.findPrices()).withTimeFrame(TimeFrame.ONE_WEEK);
    verify(priceDao.findPrices()).from(LocalDate.of(2016, 1, 1));
    verify(priceDao.findPrices()).to(LocalDate.of(2017, 12, 31));
  }

  @Test
  public void testGetWithNoQueryParams_shouldUserDefaultValues() {
    target("/price/MBT").request().get(PriceResponse.class);

    verify(priceDao.findPrices()).withSymbol("MBT");
    verify(priceDao.findPrices()).withTimeFrame(TimeFrame.ONE_DAY);
    verify(priceDao.findPrices()).from(LocalDate.now(clock).minusYears(2));
    verify(priceDao.findPrices()).to(LocalDate.now(clock));
  }

  @Test
  public void testWithNoPricesFound() {
    when(priceDao.findPrices().now()).thenReturn(Collections.emptyList());

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
