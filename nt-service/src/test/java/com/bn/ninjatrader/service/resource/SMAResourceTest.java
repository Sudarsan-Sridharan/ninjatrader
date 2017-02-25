//package com.bn.ninjatrader.service.resource;
//
//import com.bn.ninjatrader.common.data.Value;
//import com.bn.ninjatrader.common.util.TestUtil;
//import com.bn.ninjatrader.model.mongo.dao.SMADao;
//import com.bn.ninjatrader.model.request.FindRequest;
//import com.bn.ninjatrader.service.model.MultiPeriodResponse;
//import com.google.common.collect.Lists;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
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
//public class SMAResourceTest extends AbstractJerseyTest {
//
//  private static final SMADao smaDao = mock(SMADao.class);
//  private static final LocalDate date1 = LocalDate.of(2016, 2, 1);
//  private static final LocalDate date2 = LocalDate.of(2016, 2, 5);
//  private static final Value value1 = Value.of(date1, 1);
//  private static final Value value2 = Value.of(date2, 5);
//  private static final Clock fixedClock = TestUtil.fixedClock(date1);
//
//  @Override
//  protected ResourceConfig configureResource(final ResourceConfig resourceConfig) {
//    return resourceConfig.register(new SMAResource(smaDao, fixedClock));
//  }
//
//  @Before
//  public void before() {
//    when(smaDao.find(any(FindRequest.class)))
//        .thenReturn(Lists.newArrayList(value1, value2));
//  }
//
//  @After
//  public void after() {
//    Mockito.reset(smaDao);
//  }
//
//  @Test
//  public void requestValuesForOnePeriod_shouldReturnValuesForOnePeriod() {
//    final MultiPeriodResponse<Value> response = target("/sma/MEG")
//        .queryParam("timeframe", "ONE_DAY")
//        .queryParam("from", "20160101")
//        .queryParam("to", "20171231")
//        .queryParam("period", 20)
//        .request()
//        .get(MultiPeriodResponse.class);
//
//    // has only 1 period entry.
//    assertThat(response.getValues().keySet()).containsExactly(20);
//
//    // has 2 values for that period.
//    assertThat(response.getValues().get(20)).containsExactly(value1, value2);
//  }
//
//  @Test
//  public void requestValuesForMultiPeriods_shouldReturnValuesForEachPeriod() {
//    final MultiPeriodResponse response = target("/sma/MEG")
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
//    // Unknown period. Should return empty.
//    int unknownPeriod = 200;
//    assertThat(response.getValues().get(unknownPeriod)).isEmpty();
//  }
//}
