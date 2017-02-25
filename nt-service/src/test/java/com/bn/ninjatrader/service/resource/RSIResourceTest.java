//package com.bn.ninjatrader.service.resource;
//
//import com.bn.ninjatrader.model.deprecated.RSIValue;
//import com.bn.ninjatrader.common.util.TestUtil;
//import com.bn.ninjatrader.model.mongo.dao.RSIDao;
//import com.bn.ninjatrader.model.request.FindRequest;
//import com.bn.ninjatrader.service.model.MultiPeriodResponse;
//import com.google.common.collect.Lists;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import javax.ws.rs.core.GenericType;
//import java.time.Clock;
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class RSIResourceTest extends AbstractJerseyTest {
//
//  private static final RSIDao rsiDao = mock(RSIDao.class);
//  private static final LocalDate date1 = LocalDate.of(2016, 2, 1);
//  private static final LocalDate date2 = LocalDate.of(2016, 2, 5);
//  private static final RSIValue value1 = RSIValue.of(date1, 1);
//  private static final RSIValue value2 = RSIValue.of(date2, 5);
//  private static final Clock fixedClock = TestUtil.fixedClock(date1);
//
//  @Override
//  protected ResourceConfig configureResource(final ResourceConfig resourceConfig) {
//    return resourceConfig.register(new RSIResource(rsiDao, fixedClock));
//  }
//
//  @Before
//  public void before() {
//    when(rsiDao.find(any(FindRequest.class)))
//        .thenReturn(Lists.newArrayList(value1, value2));
//  }
//
//  @After
//  public void after() {
//    Mockito.reset(rsiDao);
//  }
//
//  @Test
//  public void requestValuesForOnePeriod_shouldReturnValuesForOnePeriod() {
//    final int period = 20;
//
//    final MultiPeriodResponse<RSIValue> response = target("/rsi/MEG")
//        .queryParam("timeframe", "ONE_DAY")
//        .queryParam("from", "20160101")
//        .queryParam("to", "20171231")
//        .queryParam("period", period)
//        .request()
//        .get(new GenericType<MultiPeriodResponse<RSIValue>>(){});
//
//    // has only 1 period entry.
//    assertThat(response.getValues().keySet()).hasSize(1);
//
//    // has 2 values for that period.
//    assertThat(response.getValues().get(period)).containsExactly(value1, value2);
//  }
//
//  @Test
//  public void requestValuesForMultiPeriods_shouldReturnValuesForEachPeriod() {
//    final MultiPeriodResponse<RSIValue> response = target("/rsi/MEG")
//        .queryParam("period", 20)
//        .queryParam("period", 50)
//        .queryParam("period", 100)
//        .request()
//        .get(MultiPeriodResponse.class);
//
//    // Has 3 entries, 1 for each period.
//    assertThat(response.getValues().keySet()).containsExactlyInAnyOrder(20, 50, 100);
//
//    // Each period has 2 values.
//    assertThat(response.getValues().get(20)).hasSize(2);
//    assertThat(response.getValues().get(50)).hasSize(2);
//    assertThat(response.getValues().get(100)).hasSize(2);
//
//    // Unknown Period. Should return empty.
//    int unknownPeriod = 200;
//    assertThat(response.getValues().get(unknownPeriod)).isEmpty();
//  }
//}
