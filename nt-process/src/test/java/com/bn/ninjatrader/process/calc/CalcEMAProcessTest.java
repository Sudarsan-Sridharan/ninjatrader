//package com.bn.ninjatrader.process.calc;
//
//import com.bn.ninjatrader.model.entity.Price;
//import com.bn.ninjatrader.model.deprecated.RSIValue;
//import com.bn.ninjatrader.common.data.Value;
//import com.bn.ninjatrader.model.util.TestUtil;
//import com.bn.ninjatrader.model.dao.EMADao;
//import com.bn.ninjatrader.model.dao.PriceDao;
//import com.bn.ninjatrader.model.guice.NtModelTestModule;
//import com.bn.ninjatrader.model.request.FindRequest;
//import com.bn.ninjatrader.model.request.SaveRequest;
//import com.bn.ninjatrader.adjustPrices.request.CalcRequest;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class CalcEMAProcessTest {
//  private static final Logger LOG = LoggerFactory.getLogger(CalcEMAProcessTest.class);
//
//  private final LocalDate dataStartDate = LocalDate.of(2016, 1, 1);
//  private final LocalDate dataEndDate = LocalDate.of(2016, 12, 31);
//
//  private final LocalDate fromDate = LocalDate.of(2016, 6, 1);
//  private final LocalDate toDate = LocalDate.of(2016, 12, 1);
//
//  private static PriceDao priceDao;
//  private static EMADao emaDao;
//  private static CalcEMAProcess adjustPrices;
//
//  @BeforeClass
//  public static void setup() {
//    final Injector injector = Guice.createInjector(new NtModelTestModule());
//    adjustPrices = injector.getInstance(CalcEMAProcess.class);
//    emaDao = injector.getInstance(EMADao.class);
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
//    emaDao.getMongoCollection().remove();
//    priceDao.getMongoCollection().remove();
//  }
//
//  @Test
//  public void testGetProcessName_shouldReturnEMAProcessName() {
//    assertThat(adjustPrices.getProcessName()).isEqualTo("ema");
//  }
//
//  @Test
//  public void testCalcEma_shouldSaveEmaValuesToDatabase() {
//    adjustPrices.adjustPrices(CalcRequest.forSymbol("MEG").from(dataStartDate).to(dataEndDate).periods(18));
//    assertThat(emaDao.find(FindRequest.findSymbol("MEG").from(fromDate).to(toDate).period(18)))
//        .hasSize(132);
//  }
//
//  @Test
//  public void testCalcTwice_shouldProduceSameResult() {
//    adjustPrices.adjustPrices(CalcRequest.forSymbol("MEG").from(fromDate).to(toDate).periods(18));
//    final List<RSIValue> baselineEmaValues = emaDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(18));
//
//    adjustPrices.adjustPrices(CalcRequest.forSymbol("MEG").from(fromDate).to(toDate).periods(18));
//    assertThat(emaDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(18)))
//        .isEqualTo(baselineEmaValues);
//  }
//
//  @Test
//  public void testCalcMultipleTimesWithContinuation_shouldGiveSameResultsAsFirstCalc() {
//    // Calculate EMA values from start date
//    adjustPrices.adjustPrices(CalcRequest.forSymbol("MEG").from(dataStartDate).to(toDate).periods(18));
//
//    // Baseline EMA values
//    final List<Value> baselineEmaValues = emaDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(18));
//
//    // Calculate a second time, moving the from date. It should continue from prior EMA value.
//    adjustPrices.adjustPrices(CalcRequest.forSymbol("MEG").from(fromDate.plusDays(1)).to(toDate).periods(18));
//    assertThat(emaDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(18)))
//        .isEqualTo(baselineEmaValues);
//
//    // Calculate a third time, moving the from date by 2 months. It should continue from prior EMA value.
//    adjustPrices.adjustPrices(CalcRequest.forSymbol("MEG").from(fromDate.plusMonths(2)).to(toDate).periods(18));
//    assertThat(emaDao.find(findSymbol("MEG").from(fromDate).to(toDate).period(18)))
//        .isEqualTo(baselineEmaValues);
//  }
//}
