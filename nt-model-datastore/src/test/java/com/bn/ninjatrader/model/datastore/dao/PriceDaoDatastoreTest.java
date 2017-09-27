package com.bn.ninjatrader.model.datastore.dao;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.datastore.document.PriceDocument;
import com.bn.ninjatrader.model.util.TestUtil;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.Lists;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bn.ninjatrader.model.datastore.document.PriceDocument.id;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceDaoDatastoreTest {

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final LocalDate now = LocalDate.of(2017, 1, 1);
  private final LocalDate tomorrow = now.plusDays(1);
  private final LocalDate nextMonth = now.plusMonths(1);
  private final LocalDate nextYear = now.plusYears(1);
  private final LocalDate lastYear = now.minusYears(1);

  private final Price price1 = Price.builder().date(now)
      .open(1.1).high(1.2).low(1.0).close(1.1).volume(1000).build();
  private final Price priceTomorrow = Price.builder().date(tomorrow)
      .open(2.1).high(2.2).low(2.0).close(2.1).volume(2000).build();
  private final Price priceNextMonth = Price.builder().date(nextMonth)
      .open(3.1).high(3.2).low(3.0).close(3.1).volume(3000).build();
  private final Price priceNextYear = Price.builder().date(nextYear)
      .open(4.1).high(4.2).low(4.0).close(4.1).volume(4000).build();
  private final Price priceLastYear = Price.builder().date(lastYear)
      .open(5.1).high(5.2).low(5.0).close(5.1).volume(5000).build();

  private Map<String, List<Price>> priceCache;
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
    priceCache = new HashMap<>();
    priceDao = new PriceDaoDatastore(priceCache, TestUtil.fixedClock(now));
  }

  @After
  public void after() {
    this.session.close();
    this.helper.tearDown();
  }

  @Test
  public void testSaveAndFind_shouldSaveAndReturnSortedPrices() {
    priceDao.savePrices(priceTomorrow, price1).withSymbol("MEG").now();

    // Find prices
    final List<Price> prices = priceDao.findPrices().withSymbol("MEG")
        .from(LocalDate.of(2017, 1, 1))
        .to(LocalDate.of(2017, 12, 31))
        .withTimeFrame(TimeFrame.ONE_DAY)
        .now();

    // Verify values are sorted
    assertThat(prices).containsExactly(price1, priceTomorrow);
  }

  @Test
  public void testSavePricesWithDiffYears_shouldSaveToEachDocumentPerYear() {
    priceDao.savePrices(price1, priceTomorrow, priceNextMonth, priceNextYear).withSymbol("MEG").now();

    // Find all prices
    final List<Price> prices = priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).now();

    // Verify all prices found
    assertThat(prices).containsExactly(price1, priceTomorrow, priceNextMonth, priceNextYear);
  }

  @Test
  public void testFindPricesWithinDateRange_shouldReturnPricesWithinDateRangeOnly() {
    // Save all prices
    priceDao.savePrices(price1, priceTomorrow, priceNextMonth, priceNextYear).withSymbol("MEG").now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(now).now())
        .containsExactly(price1);

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(tomorrow).now())
        .containsExactly(price1, priceTomorrow);

    assertThat(priceDao.findPrices().withSymbol("MEG").from(tomorrow).to(nextMonth).now())
        .containsExactly(priceTomorrow, priceNextMonth);

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now.minusYears(2)).to(now.minusYears(1)).now())
        .isEmpty();
  }

  @Test
  public void testFindWithEmptyCache_shouldRetrieveFromDatabaseAndFillCache() {
    priceDao.savePrices(price1, priceNextMonth, priceNextYear, priceTomorrow).withSymbol("MEG").now();

    // Assert that cache is updated with saved prices
    assertThat(priceCache.get(id("MEG", now.getYear(), TimeFrame.ONE_DAY)))
        .containsExactly(price1, priceTomorrow, priceNextMonth);
    assertThat(priceCache.get(id("MEG", nextYear.getYear(), TimeFrame.ONE_DAY))).containsExactly(priceNextYear);

    // Clear cache after save.
    priceCache.clear();

    // Find prices. Should return merged results from cache and database prices
    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).now())
        .containsExactly(price1, priceTomorrow, priceNextMonth, priceNextYear);

    // Assert that cache is updated with missing prices
    assertThat(priceCache.get(id("MEG", now.getYear(), TimeFrame.ONE_DAY)))
        .containsExactly(price1, priceTomorrow, priceNextMonth);
    assertThat(priceCache.get(id("MEG", nextYear.getYear(), TimeFrame.ONE_DAY))).containsExactly(priceNextYear);
  }

  @Test
  public void testFindWithCache_shouldRetrievePricesInCache() {
    priceDao.savePrices(priceLastYear, priceNextYear).withSymbol("MEG").now();

    priceCache.clear();

    // Prepare cache with partial prices only
    priceCache.put(id("MEG", now.getYear(), TimeFrame.ONE_DAY), Lists.newArrayList(price1, priceTomorrow));

    // Find prices. Should return merged results from cache and database prices
    assertThat(priceDao.findPrices().withSymbol("MEG").from(lastYear).to(nextYear).now())
        .containsExactly(priceLastYear, price1, priceTomorrow, priceNextYear);

    // Assert that cache is updated with missing prices
    assertThat(priceCache.get(id("MEG", lastYear.getYear(), TimeFrame.ONE_DAY))).containsExactly(priceLastYear);
    assertThat(priceCache.get(id("MEG", nextYear.getYear(), TimeFrame.ONE_DAY))).containsExactly(priceNextYear);
  }

  @Test
  public void testSaveWithOverwrite_shouldNotContainDuplicatePricesWithEqualDate() {
    // Save prices
    priceDao.savePrices(price1, priceTomorrow).withSymbol("MEG").now();

    // Overwrite priceTomorrow
    priceDao.savePrices(priceTomorrow, priceNextMonth).withSymbol("MEG").now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextMonth).now())
        .containsExactly(price1, priceTomorrow, priceNextMonth);
  }

  @Test
  public void testFindNonExistingPrices_shouldReturnEmpty() {
    // Save prices
    priceDao.savePrices(price1, priceTomorrow).withSymbol("MEG").now();

    assertThat(priceDao.findPrices().withSymbol("BDO").from(now).to(now).now()).isEmpty();
  }

  @Test
  public void testFindWithDiffSymbol_shouldReturnPricesForGivenSymbol() {
    // Save prices
    priceDao.savePrices(price1, priceTomorrow).withSymbol("MEG").now();
    priceDao.savePrices(priceTomorrow, priceNextMonth).withSymbol("BDO").now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).now())
        .containsExactly(price1, priceTomorrow);
    assertThat(priceDao.findPrices().withSymbol("BDO").from(now).to(nextYear).now())
        .containsExactly(priceTomorrow, priceNextMonth);
  }

  @Test
  public void testFindWithDiffTimeframe_shouldReturnPricesForGivenTimeframe() {
    // Save prices
    priceDao.savePrices(price1, priceTomorrow).withSymbol("MEG").now();
    priceDao.savePrices(priceTomorrow, priceNextMonth).withSymbol("MEG").withTimeFrame(TimeFrame.ONE_WEEK).now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).now())
        .containsExactly(price1, priceTomorrow);
    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).withTimeFrame(TimeFrame.ONE_WEEK).now())
        .containsExactly(priceTomorrow, priceNextMonth);
  }

  @Test
  public void testFindAllSymbols_shouldReturnAllSymbolsOfCurrentYear() {
    priceDao.savePrices(price1).withSymbol("MEG").now();
    priceDao.savePrices(priceTomorrow).withSymbol("BDO").now();
    priceDao.savePrices(priceNextYear).withSymbol("TEL").now(); // price of next year

    assertThat(priceDao.findAllSymbols()).containsExactlyInAnyOrder("MEG", "BDO");
  }
}
