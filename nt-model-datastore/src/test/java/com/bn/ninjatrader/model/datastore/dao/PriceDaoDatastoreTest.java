package com.bn.ninjatrader.model.datastore.dao;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.datastore.document.PriceDocument;
import com.bn.ninjatrader.model.datastore.factory.PriceBuilderFactoryDatastore;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.util.TestUtil;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceDaoDatastoreTest {

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final PriceBuilderFactory pbf = new PriceBuilderFactoryDatastore();
  private final LocalDate now = LocalDate.of(2017, 1, 1);
  private final LocalDate tomorrow = now.plusDays(1);
  private final LocalDate nextMonth = now.plusMonths(1);
  private final LocalDate nextYear = now.plusYears(1);

  private final Price price1 = pbf.builder().date(now)
      .open(1.1).high(1.2).low(1.0).close(1.1).volume(1000).build();
  private final Price price2 = pbf.builder().date(tomorrow)
      .open(2.1).high(2.2).low(2.0).close(2.1).volume(2000).build();
  private final Price price3 = pbf.builder().date(nextMonth)
      .open(3.1).high(3.2).low(3.0).close(3.1).volume(3000).build();
  private final Price price4 = pbf.builder().date(nextYear)
      .open(4.1).high(4.2).low(4.0).close(4.1).volume(4000).build();

  private Closeable session;
  private PriceDaoDatastore priceDao;

  @BeforeClass
  public static void setUpBeforeClass() {
    ObjectifyService.setFactory(new ObjectifyFactory());
    ObjectifyService.register(PriceDocument.class);
  }

  @Before
  public void before() {
    this.session = ObjectifyService.begin();
    this.helper.setUp();
    priceDao = new PriceDaoDatastore(TestUtil.fixedClock(now));
  }

  @After
  public void after() {
    this.session.close();
    this.helper.tearDown();
  }

  @Test
  public void testSaveAndFind_shouldSaveAndReturnSortedPrices() {
    priceDao.savePrices(price2, price1).withSymbol("MEG").now();

    // Find prices
    final List<Price> prices = priceDao.findPrices().withSymbol("MEG")
        .from(LocalDate.of(2017, 1, 1))
        .to(LocalDate.of(2017, 12, 31))
        .withTimeFrame(TimeFrame.ONE_DAY)
        .now();

    // Verify values are sorted
    assertThat(prices).containsExactly(price1, price2);
  }

  @Test
  public void testSavePricesWithDiffYears_shouldSaveToEachDocumentPerYear() {
    priceDao.savePrices(price1, price2, price3, price4).withSymbol("MEG").now();

    // Find all prices
    final List<Price> prices = priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).now();

    // Verify all prices found
    assertThat(prices).containsExactly(price1, price2, price3, price4);
  }

  @Test
  public void testFindPricesWithinDateRange_shouldReturnPricesWithinDateRangeOnly() {
    // Save all prices
    priceDao.savePrices(price1, price2, price3, price4).withSymbol("MEG").now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(now).now())
        .containsExactly(price1);

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(tomorrow).now())
        .containsExactly(price1, price2);

    assertThat(priceDao.findPrices().withSymbol("MEG").from(tomorrow).to(nextMonth).now())
        .containsExactly(price2, price3);

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now.minusYears(2)).to(now.minusYears(1)).now())
        .isEmpty();
  }

  @Test
  public void testSaveWithOverwrite_shouldNotContainDuplicatePricesWithEqualDate() {
    // Save prices
    priceDao.savePrices(price1, price2).withSymbol("MEG").now();

    // Overwrite price2
    priceDao.savePrices(price2, price3).withSymbol("MEG").now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextMonth).now())
        .containsExactly(price1, price2, price3);
  }

  @Test
  public void testFindNonExistingPrices_shouldReturnEmpty() {
    // Save prices
    priceDao.savePrices(price1, price2).withSymbol("MEG").now();

    assertThat(priceDao.findPrices().withSymbol("BDO").from(now).to(now).now()).isEmpty();
  }

  @Test
  public void testFindWithDiffSymbol_shouldReturnPricesForGivenSymbol() {
    // Save prices
    priceDao.savePrices(price1, price2).withSymbol("MEG").now();
    priceDao.savePrices(price2, price3).withSymbol("BDO").now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).now())
        .containsExactly(price1, price2);
    assertThat(priceDao.findPrices().withSymbol("BDO").from(now).to(nextYear).now())
        .containsExactly(price2, price3);
  }

  @Test
  public void testFindWithDiffTimeframe_shouldReturnPricesForGivenTimeframe() {
    // Save prices
    priceDao.savePrices(price1, price2).withSymbol("MEG").now();
    priceDao.savePrices(price2, price3).withSymbol("MEG").withTimeFrame(TimeFrame.ONE_WEEK).now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).now())
        .containsExactly(price1, price2);
    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).withTimeFrame(TimeFrame.ONE_WEEK).now())
        .containsExactly(price2, price3);
  }

  @Test
  public void testFindAllSymbols_shouldReturnAllSymbolsOfCurrentYear() {
    priceDao.savePrices(price1).withSymbol("MEG").now();
    priceDao.savePrices(price2).withSymbol("BDO").now();
    priceDao.savePrices(price4).withSymbol("TEL").now(); // price of next year

    assertThat(priceDao.findAllSymbols()).containsExactlyInAnyOrder("MEG", "BDO");
  }
}
