package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.rest.PriceResponse;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.bn.ninjatrader.service.resource.PriceResource;
import com.google.common.collect.Lists;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.ws.rs.NotFoundException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceResourceTest {

  private static final PriceDao priceDao = mock(PriceDao.class);
  private static final LocalDate date1 = LocalDate.of(2016, 2, 1);
  private static final LocalDate date2 = LocalDate.of(2016, 2, 5);
  private static final Price price1 = new Price(date1, 1, 2, 3, 4, 10000);
  private static final Price price2 = new Price(date2, 5, 6, 7, 8, 20000);

  private static final ZoneId zoneId = ZoneId.systemDefault();
  private static final Clock fixedClock = Clock.fixed(date1.atStartOfDay(zoneId).toInstant(), zoneId);

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new PriceResource(priceDao, fixedClock))
      .addProvider(LocalDateParamConverterProvider.class)
      .build();

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
    PriceResponse priceResponse = resources.client()
        .target("/price/MEG?timeframe=ONE_WEEK&from=20160101&to=20171231")
        .request()
        .get(PriceResponse.class);

    assertThat(priceResponse.getPriceList()).hasSize(2);
    assertThat(priceResponse.getPriceList().get(0)).isEqualTo(price1);
    assertThat(priceResponse.getPriceList().get(1)).isEqualTo(price2);
    assertThat(priceResponse.getFromDate()).isEqualTo(date1);
    assertThat(priceResponse.getToDate()).isEqualTo(date2);
  }

  @Test
  public void testQueryParams() {
    ArgumentCaptor<FindRequest> findRequestCaptor = ArgumentCaptor.forClass(FindRequest.class);

    resources.client()
        .target("/price/BDO?timeframe=ONE_WEEK&from=20160101&to=20171231")
        .request()
        .get(PriceResponse.class);

    verify(priceDao, times(1)).find(findRequestCaptor.capture());

    FindRequest findRequest = findRequestCaptor.getValue();
    assertThat(findRequest.getSymbol()).isEqualTo("BDO");
    assertThat(findRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_WEEK);
    assertThat(findRequest.getFromDate()).isEqualTo(LocalDate.of(2016, 1, 1));
    assertThat(findRequest.getToDate()).isEqualTo(LocalDate.of(2017, 12, 31));
  }

  @Test
  public void testDefaultQueryParams() {
    ArgumentCaptor<FindRequest> findRequestCaptor = ArgumentCaptor.forClass(FindRequest.class);

    resources.client().target("/price/MBT").request().get(PriceResponse.class);

    verify(priceDao, times(1)).find(findRequestCaptor.capture());

    FindRequest findRequest = findRequestCaptor.getValue();
    assertThat(findRequest.getSymbol()).isEqualTo("MBT");
    assertThat(findRequest.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);
    assertThat(findRequest.getFromDate()).isEqualTo(LocalDate.now(fixedClock).minusYears(2));
    assertThat(findRequest.getToDate()).isEqualTo(LocalDate.now(fixedClock));
  }

  @Test
  public void testWithNoPricesFound() {
    when(priceDao.find(any(FindRequest.class)))
        .thenReturn(Collections.emptyList());

    PriceResponse priceResponse = resources.client().target("/price/MBT").request().get(PriceResponse.class);

    assertThat(priceResponse.getFromDate()).isNull();
    assertThat(priceResponse.getToDate()).isNull();
    assertThat(priceResponse.getPriceList()).isEmpty();
  }

  @Test(expected = NotFoundException.class)
  public void testInvalidPath() {
    resources.client()
        .target("/price")
        .request()
        .get(PriceResponse.class);
  }
}
