package com.bn.ninjatrader.model.dao;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.data.PriceData;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by Brad on 5/4/16.
 */
public class PriceDaoTest extends AbstractDaoTest {
  private static final Logger log = LoggerFactory.getLogger(PriceDaoTest.class);

  private PriceDao priceDao;

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final LocalDate dateWithNoData = LocalDate.of(2000, 1, 1);

  @BeforeClass
  public void setup() {
    priceDao = injector.getInstance(PriceDao.class);
  }

  @BeforeMethod
  public void cleanup() {
    MongoCollection collection = priceDao.getMongoCollection();
    collection.remove();
  }

  @Test
  public void testSaveAndFind() {
    // Prepare data
    PriceData priceData = new PriceData("MEG", 2016);
    Price price1 = new Price(now, 1.1, 1.2, 1.0, 1.1, 10000);
    Price price2 = new Price(now.plusDays(1), 2.1, 2.2, 2.0, 2.1, 20000);

    // Add Price data for January 1 and 2, 2016
    priceData.getData().add(price1);
    priceData.getData().add(price2);

    // Save Price data
    priceDao.save(priceData);

    // Find data
    List<PriceData> result = priceDao.find();
    assertNotNull(result);
    assertEquals(result.size(), 1);

    // Verify data
    PriceData resultData = result.get(0);
    assertEquals(resultData.getSymbol(), priceData.getSymbol());
    assertEquals(resultData.getYear(), priceData.getYear());
    assertEquals(resultData.getData().size(), 2);

    // Verify Price 1
    TestUtil.assertPriceEquals(resultData.getData().get(0), price1);

    // Verify Price 2
    TestUtil.assertPriceEquals(resultData.getData().get(1), price2);
  }

  @Test
  public void testFindByDateRange() {
    // Prepare data
    LocalDate date1 = LocalDate.of(2014, 1, 1);
    LocalDate date2 = LocalDate.of(2016, 2, 2);

    PriceData priceData = new PriceData("MEG", 2014);
    Price price1 = new Price(date1, 1.1, 1.2, 1.0, 1.1, 10000);
    priceData.getData().add(price1);

    PriceData priceData2 = new PriceData("MEG", 2016);
    Price price2 = new Price(date2, 2.1, 2.2, 2.0, 2.1, 20000);
    priceData2.getData().add(price2);

    // Save Price data
    priceDao.save(priceData);
    priceDao.save(priceData2);

    List<Price> result = priceDao.findByDateRange("MEG", date1, date2);
    assertEquals(result.size(), 2);
    assertEquals(result.get(0).getDate(), date1);
    assertEquals(result.get(1).getDate(), date2);

    result = priceDao.findByDateRange("MEG", date1.plusDays(1), date2);
    assertEquals(result.size(), 1);

    result = priceDao.findByDateRange("MEG", date1.plusDays(1), date2.minusDays(1));
    assertEquals(result.size(), 0);

    result = priceDao.findByDateRange("MEG", dateWithNoData, dateWithNoData.plusDays(1));
    assertEquals(result.size(), 0);
  }

  @Test
  public void testFindAllSymbols() {
    // Prepare data
    Price price1 = new Price(LocalDate.now(), 1.1, 1.2, 1.0, 1.1, 10000);
    Price price2 = new Price(LocalDate.now(), 2.1, 2.2, 2.0, 2.1, 20000);

    priceDao.save("MEG", Lists.newArrayList(price1));
    priceDao.save("BDO", Lists.newArrayList(price2));

    List<String> symbols = priceDao.findAllSymbols();
    assertEquals(symbols.size(), 2);
    assertTrue(symbols.contains("MEG"));
    assertTrue(symbols.contains("BDO"));
  }

  @Test
  public void testSort() {
    Price price1 = new Price(now, 1.1, 1.2, 1.0, 1.1, 10000);
    Price price2 = new Price(now.plusDays(1), 2.1, 2.2, 2.0, 2.1, 20000);

    priceDao.save("MEG", Lists.newArrayList(price2, price1));

    List<Price> foundPrices = priceDao.findByYear("MEG", 2016);

    TestUtil.assertPriceEquals(foundPrices.get(0), price1);
    TestUtil.assertPriceEquals(foundPrices.get(1), price2);
  }

  @Test
  public void testSaveWithOverlap() {
    // Set 1
    Price price1 = new Price(now, 1.1, 1.2, 1.0, 1.1, 10000);
    Price price2 = new Price(now.plusDays(1), 2.1, 2.2, 2.0, 2.1, 20000);
    Price price3 = new Price(now.plusDays(2), 3.1, 3.2, 3.3, 3.4, 30000);

    // Set 2
    Price price4 = new Price(now.plusDays(2), 4.1, 4.2, 4.3, 4.4, 40000);
    Price price5 = new Price(now.plusDays(3), 5.1, 5.2, 5.3, 5.4, 50000);

    // Save set 1 prices
    priceDao.save("MEG", Lists.newArrayList(price3, price2, price1));

    // Find data
    List<Price> foundPrices = priceDao.findByYear("MEG", 2016);
    assertEquals(foundPrices.size(), 3);

    // Verify data is sorted
    TestUtil.assertPriceEquals(foundPrices.get(0), price1);
    TestUtil.assertPriceEquals(foundPrices.get(1), price2);
    TestUtil.assertPriceEquals(foundPrices.get(2), price3);

    // Save set 2 prices. price4 overwrites price3
    priceDao.save("MEG", Lists.newArrayList(price1, price4, price5));

    // Find data
    foundPrices = priceDao.findByYear("MEG", 2016);
    assertEquals(foundPrices.size(), 4);
    TestUtil.assertPriceEquals(foundPrices.get(2), price4);
  }

  @Test
  public void testSaveMultipleYears() {
    // Year 1
    LocalDate date1 = LocalDate.of(2014, 1, 1);
    Price price1 = new Price(date1, 1.1, 1.2, 1.0, 1.1, 10000);

    // Year 2
    LocalDate date2 = LocalDate.of(2015, 1, 1);
    Price price2 = new Price(date2, 2.1, 2.2, 2.0, 2.1, 20000);
    Price price3 = new Price(date2.plusDays(1), 3.1, 3.2, 3.3, 3.4, 30000);

    // Year 3
    LocalDate date3 = LocalDate.of(2016, 12, 29);
    Price price4 = new Price(date3, 4.1, 4.2, 4.3, 4.4, 40000);
    Price price5 = new Price(date3.plusDays(1), 5.1, 5.2, 5.3, 5.4, 50000);
    Price price6 = new Price(date3.plusDays(2), 6.1, 6.2, 6.3, 6.4, 60000);

    // Add in any order
    List<Price> prices = Lists.newArrayList(price5, price3, price4, price2, price6, price1);

    priceDao.save("MEG", prices);

    // Verify Year 1
    List<Price> foundPrices = priceDao.findByYear("MEG", 2014);
    assertEquals(foundPrices.size(), 1);
    TestUtil.assertPriceEquals(foundPrices.get(0), price1);

    // Verify Year 2
    foundPrices = priceDao.findByYear("MEG", 2015);
    assertEquals(foundPrices.size(), 2);
    TestUtil.assertPriceEquals(foundPrices.get(0), price2);
    TestUtil.assertPriceEquals(foundPrices.get(1), price3);

    // Verify Year 3
    foundPrices = priceDao.findByYear("MEG", 2016);
    assertEquals(foundPrices.size(), 3);
    TestUtil.assertPriceEquals(foundPrices.get(0), price4);
    TestUtil.assertPriceEquals(foundPrices.get(1), price5);
    TestUtil.assertPriceEquals(foundPrices.get(2), price6);
  }

  @Test
  public void testRemoveByDates() {
    // Year 1
    LocalDate date1 = LocalDate.of(2014, 1, 1);
    Price price1 = new Price(date1, 1.1, 1.2, 1.0, 1.1, 10000);

    // Year 2
    LocalDate date2 = LocalDate.of(2015, 1, 1);
    Price price2 = new Price(date2, 2.1, 2.2, 2.0, 2.1, 20000);
    Price price3 = new Price(date2.plusDays(1), 3.1, 3.2, 3.3, 3.4, 30000);

    // Year 3
    LocalDate date3 = LocalDate.of(2016, 12, 29);
    Price price4 = new Price(date3, 4.1, 4.2, 4.3, 4.4, 40000);
    Price price5 = new Price(date3.plusDays(1), 5.1, 5.2, 5.3, 5.4, 50000);
    Price price6 = new Price(date3.plusDays(2), 6.1, 6.2, 6.3, 6.4, 60000);

    // Save prices
    List<Price> prices = Lists.newArrayList(price1, price2, price3, price4, price5, price6);
    priceDao.save("MEG", prices);

    // Remove by dates
    List<LocalDate> removeDates = Lists.newArrayList(price2.getDate(), price6.getDate(), price1.getDate());
    priceDao.removeByDates("MEG", removeDates);

    // Verify results. 3 removed, 3 remaining
    List<Price> result = priceDao.findByDateRange("MEG", date1, price6.getDate());
    assertEquals(result.size(), 3);
    TestUtil.assertPriceEquals(result.get(0), price3);
    TestUtil.assertPriceEquals(result.get(1), price4);
    TestUtil.assertPriceEquals(result.get(2), price5);
  }

  @Test
  public void testRemoveByDatesForAllSymbols() {
    LocalDate date1 = LocalDate.of(2014, 1, 1);
    Price price1 = new Price(date1, 1.1, 1.2, 1.0, 1.1, 10000);
    Price price2 = new Price(date1, 2.1, 2.2, 2.0, 2.1, 20000);
    Price price3 = new Price(date1, 3.1, 3.2, 3.3, 3.4, 30000);
    Price price4 = new Price(date1.plusDays(1), 4.1, 4.2, 4.3, 4.4, 40000);

    priceDao.save("MEG", Lists.newArrayList(price1));
    priceDao.save("BDO", Lists.newArrayList(price2));
    priceDao.save("MBT", Lists.newArrayList(price3));
    priceDao.save("MEG", Lists.newArrayList(price4)); // Should remain

    log.debug("Removing date: {}", date1);
    priceDao.removeByDates(Lists.newArrayList(date1));

    List<PriceData> results = priceDao.find();
    log.debug("Found Result: {}", results);
    assertEquals(results.size(), 3);

    int successCode = 0;
    for (PriceData data : results) {
      if (data.getSymbol().equals("BDO")) {
        assertEquals(data.getData().size(), 0);
        successCode += 1;
      } else if (data.getSymbol().equals("MBT")) {
        assertEquals(data.getData().size(), 0);
        successCode += 10;
      } else if (data.getSymbol().equals("MEG")) {
        assertEquals(data.getData().size(), 1);
        TestUtil.assertPriceEquals(data.getData().get(0), price4);
        successCode += 100;
      }
    }
    assertEquals(successCode, 111, "PriceData must contain BDO, MBT, and MEG");
  }

  @Test
  public void testFindNBarsBeforeDate() {
    List<Price> prices = TestUtil.randomPricesForDateRange(now.minusYears(1), now);

    priceDao.save("MEG", prices);

    // Test with 1 bar
    List<Price> result = priceDao.findNBarsBeforeDate("MEG", 1, now);
    assertEquals(result.size(), 1);
    assertEquals(result.get(0), getLastPrice(prices));

    // Test with 30 bars
    result = priceDao.findNBarsBeforeDate("MEG", 30, now);
    assertEquals(result.size(), 30);
    assertEquals(getLastPrice(result), getLastPrice(prices));
    assertAllPriceDatesIsBeforeDate(result, now);

    // Test with past date
    LocalDate pastDate = LocalDate.of(2015, 12, 29);
    result = priceDao.findNBarsBeforeDate("MEG", 30, pastDate);
    assertEquals(result.size(), 30);
    assertEquals(getLastPrice(result).getDate(), pastDate);
    assertAllPriceDatesIsBeforeDate(result, pastDate);
  }

  @Test
  public void testFindNBarsBeforeDateWithMultipleYears() {
    List<Price> prices = TestUtil.randomPricesForDateRange(now.minusYears(2), now);

    priceDao.save("MEG", prices);

    // Test with 400 bars
    List<Price> result = priceDao.findNBarsBeforeDate("MEG", 400, now);
    assertEquals(result.size(), 400);
    assertEquals(getLastPrice(result), getLastPrice(prices));
    assertEquals(result.get(0).getDate(), LocalDate.of(2014, 6, 20));
  }

  private Price getLastPrice(List<Price> prices) {
    return prices.get(prices.size() - 1);
  }

  private void assertAllPriceDatesIsBeforeDate(List<Price> prices, LocalDate date) {
    for (Price price : prices) {
      LocalDate priceDate = price.getDate();
      assertTrue(priceDate.isBefore(date) || priceDate.isEqual(date), price + " should be <= " + date);
    }
  }
}
