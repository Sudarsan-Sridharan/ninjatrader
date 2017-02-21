package com.bn.ninjatrader.model.dao.datastore;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.appengine.PriceDocument;
import com.bn.ninjatrader.model.appengine.request.FindPriceRequest;
import com.bn.ninjatrader.model.appengine.request.SavePriceRequest;
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

  private final LocalDate now = LocalDate.of(2017, 1, 1);
  private final LocalDate tomorrow = now.plusDays(1);
  private final LocalDate nextMonth = now.plusMonths(1);
  private final LocalDate nextYear = now.plusYears(1);

  private final Price price1 = Price.builder().date(now)
      .open(1.1).high(1.2).low(1.0).close(1.1).volume(1000).build();
  private final Price price2 = Price.builder().date(tomorrow)
      .open(2.1).high(2.2).low(2.0).close(2.1).volume(2000).build();
  private final Price price3 = Price.builder().date(nextMonth)
      .open(3.1).high(3.2).low(3.0).close(3.1).volume(3000).build();
  private final Price price4 = Price.builder().date(nextYear)
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
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price2, price1));

    // Find prices
    final List<Price> prices = priceDao.find(FindPriceRequest.forSymbol("MEG")
        .from(LocalDate.of(2017, 1, 1))
        .to(LocalDate.of(2017, 12, 31))
        .timeframe(TimeFrame.ONE_DAY));

    // Verify values are sorted
    assertThat(prices).containsExactly(price1, price2);
  }

  @Test
  public void testSavePricesWithDiffYears_shouldSaveToEachDocumentPerYear() {
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price1, price2, price3, price4));

    // Find all prices
    final List<Price> prices = priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(nextYear));

    // Verify all prices found
    assertThat(prices).containsExactly(price1, price2, price3, price4);
  }

  @Test
  public void testFindPricesWithinDateRange_shouldReturnPricesWithinDateRangeOnly() {
    // Save all prices
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price1, price2, price3, price4));

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(now)))
        .containsExactly(price1);

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(tomorrow)))
        .containsExactly(price1, price2);

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(tomorrow).to(nextMonth)))
        .containsExactly(price2, price3);

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now.minusYears(2)).to(now.minusYears(1))))
        .isEmpty();
  }

  @Test
  public void testSaveWithOverwrite_shouldNotContainDuplicatePricesWithEqualDate() {
    // Save prices
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price1, price2));

    // Overwrite price2
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price2, price3));

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(nextMonth)))
        .containsExactly(price1, price2, price3);
  }

  @Test
  public void testFindNonExistingPrices_shouldReturnEmpty() {
    // Save prices
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price1, price2));

    assertThat(priceDao.find(FindPriceRequest.forSymbol("BDO").from(now).to(now))).isEmpty();
  }

  @Test
  public void testFindWithDiffSymbol_shouldReturnPricesForGivenSymbol() {
    // Save prices
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price1, price2));
    priceDao.save(SavePriceRequest.forSymbol("BDO").addPrices(price2, price3));

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(nextYear)))
        .containsExactly(price1, price2);
    assertThat(priceDao.find(FindPriceRequest.forSymbol("BDO").from(now).to(nextYear)))
        .containsExactly(price2, price3);
  }

  @Test
  public void testFindWithDiffTimeframe_shouldReturnPricesForGivenTimeframe() {
    // Save prices
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price1, price2));
    priceDao.save(SavePriceRequest.forSymbol("MEG").timeframe(TimeFrame.ONE_WEEK).addPrices(price2, price3));

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(nextYear)))
        .containsExactly(price1, price2);
    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(nextYear).timeframe(TimeFrame.ONE_WEEK)))
        .containsExactly(price2, price3);
  }

  @Test
  public void testFindAllSymbols_shouldReturnAllSymbolsOfCurrentYear() {
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price1));
    priceDao.save(SavePriceRequest.forSymbol("BDO").addPrices(price2));
    priceDao.save(SavePriceRequest.forSymbol("TEL").addPrices(price4)); // price of next year

    assertThat(priceDao.findAllSymbols()).containsExactlyInAnyOrder("MEG", "BDO");
  }
}
