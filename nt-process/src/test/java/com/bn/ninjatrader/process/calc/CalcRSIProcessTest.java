//package com.bn.ninjatrader.process.calc;
//
//import com.bn.ninjatrader.calculator.parameter.CalcParams;
//import com.bn.ninjatrader.common.data.Price;
//import com.bn.ninjatrader.model.deprecated.RSIValue;
//import com.bn.ninjatrader.common.util.TestUtil;
//import com.bn.ninjatrader.model.dao.PriceDao;
//import com.bn.ninjatrader.model.dao.RSIDao;
//import com.bn.ninjatrader.model.guice.NtModelTestModule;
//import com.bn.ninjatrader.model.request.SaveRequest;
//import com.bn.ninjatrader.process.request.CalcRequest;
//import com.google.common.collect.Lists;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import org.junit.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static com.bn.ninjatrader.common.type.TimeFrame.ONE_DAY;
//import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * Created by Brad on 6/11/16.
// */
//public class CalcRSIProcessTest {
//  private static final Logger LOG = LoggerFactory.getLogger(CalcRSIProcessTest.class);
//
//  private final int period = 14;
//
//  private final LocalDate dataStartDate = LocalDate.of(2016, 1, 1);
//  private final LocalDate dataEndDate = LocalDate.of(2016, 12, 31);
//
//  private final LocalDate fromDate = LocalDate.of(2016, 6, 1);
//  private final LocalDate toDate = LocalDate.of(2016, 12, 1);
//
//  private static CalcRSIProcess process;
//  private static RSIDao rsiDao;
//  private static PriceDao priceDao;
//
//  @BeforeClass
//  public static void setup() {
//    final Injector injector = Guice.createInjector(new NtModelTestModule());
//    process = injector.getInstance(CalcRSIProcess.class);
//    rsiDao = injector.getInstance(RSIDao.class);
//    priceDao = injector.getInstance(PriceDao.class);
//  }
//
//  @Before
//  public void before() {
//    final List<Price> dummyPrices = TestUtil.randomPricesForDateRange(dataStartDate, dataEndDate);
//    priceDao.save(SaveRequest.save("MEG").values(dummyPrices));
//  }
//
//  @After
//  public void cleanup() {
//    rsiDao.getMongoCollection().remove();
//    priceDao.getMongoCollection().remove();
//  }
//
//  @Test
//  public void testCalcParamsWithNoExistingRSIValues_shouldHaveNoContinueFromValue() {
//    final List<Price> prices = priceDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(period));
//    final CalcParams params =
//        process.provideCalcParams("MEG", ONE_DAY, prices, Lists.newArrayList(period));
//
//    assertThat(params.getPrices()).isEqualTo(prices);
//    assertThat(params.getPeriods()).containsExactly(14);
//    assertThat(params.getPriorValueForPeriod(14)).isEmpty();
//  }
//
//  @Test
//  public void testCalcParamsWithExistingRSIValues_shouldContinueFromExistingRSIValues() {
//    // Add RSI values to db first
//    process.process(CalcRequest.forSymbol("MEG").from(fromDate).to(toDate).periods(period));
//
//    // Retrieve RSIValue to continue calculating from
//    final RSIValue expectedValueToContinueFrom = rsiDao.find(findSymbol("MEG")
//        .from(fromDate).to(fromDate).period(period)).get(0);
//
//    // Move search date by 1 day
//    final LocalDate fromDate = this.fromDate.plusDays(1);
//    final List<Price> prices = priceDao.find(findSymbol("MEG").from(fromDate).to(toDate));
//
//    // Get CalcParams
//    final CalcParams<RSIValue> params = process.provideCalcParams("MEG", ONE_DAY, prices, Lists.newArrayList(period));
//
//    // Verify values
//    assertThat(params.getPrices()).isEqualTo(prices);
//    assertThat(params.getPeriods()).containsExactly(14);
//    assertThat(params.getPriorValueForPeriod(14)).isPresent();
//
//    // Verify RSIValue to continue from
//    final RSIValue value = params.getPriorValueForPeriod(14).get();
//    assertThat(value).isEqualTo(expectedValueToContinueFrom);
//  }
//
//  @Test
//  public void testCalcTwice_shouldProduceSameResult() {
//    process.process(CalcRequest.forSymbol("MEG").from(fromDate).to(toDate).periods(14));
//    final List<RSIValue> rsiValues1 = rsiDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(14));
//
//    process.process(CalcRequest.forSymbol("MEG").from(fromDate).to(toDate).periods(14));
//    final List<RSIValue> rsiValues2 = rsiDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(14));
//
//    assertThat(rsiValues1).isEqualTo(rsiValues2);
//  }
//
//  @Test
//  public void testCalcMultipleTimesWithContinuation_shouldGiveSameResultsAsFirstCalc() {
//    // Calculate RSI values from start date
//    process.process(CalcRequest.forSymbol("MEG").from(dataStartDate).to(toDate).periods(14));
//
//    // Baseline RSI values
//    final List<RSIValue> rsiValues1 = rsiDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(14));
//
//    // Calculate a second time, moving the from date. It should continue from prior RSI value.
//    process.process(CalcRequest.forSymbol("MEG").from(fromDate.plusDays(1)).to(toDate).periods(14));
//    final List<RSIValue> rsiValues2 = rsiDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(14));
//    assertThat(rsiValues1).isEqualTo(rsiValues2);
//
//    // Calculate a third time, moving from date by 2 months. It should continue from prior RSI value.
//    process.process(CalcRequest.forSymbol("MEG").from(fromDate.plusMonths(2)).to(toDate).periods(14));
//    final List<RSIValue> rsiValues3 = rsiDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(14));
//    assertThat(rsiValues1).isEqualTo(rsiValues3);
//  }
//}
