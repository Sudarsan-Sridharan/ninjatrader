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
import java.util.Optional;

import static org.testng.Assert.*;

/**
 * Created by Brad on 5/4/16.
 */
public class PriceDaoTest extends AbstractDaoTest {
  private static final Logger log = LoggerFactory.getLogger(PriceDaoTest.class);

  private PriceDao priceDao;

  private final LocalDate now = LocalDate.of(2016, 1, 1);

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
    Price price1 = new Price(1.1, 1.2, 1.0, 1.1, 10000, now);
    Price price2 = new Price(2.1, 2.2, 2.0, 2.1, 20000, now.plusDays(1));

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
    Price price1 = new Price(1.1, 1.2, 1.0, 1.1, 10000, date1);
    priceData.getData().add(price1);

    PriceData priceData2 = new PriceData("MEG", 2016);
    Price price2 = new Price(2.1, 2.2, 2.0, 2.1, 20000, date2);
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
  }

  @Test
  public void testFindAllSymbols() {
    // Prepare data
    PriceData priceData = new PriceData("MEG", 2016);
    Price price1 = new Price(1.1, 1.2, 1.0, 1.1, 10000, LocalDate.of(2014, 1, 1));
    priceData.getData().add(price1);

    PriceData priceData2 = new PriceData("BDO", 2016);
    Price price2 = new Price(2.1, 2.2, 2.0, 2.1, 20000, LocalDate.of(2016, 1, 1));
    priceData2.getData().add(price2);

    priceDao.save(priceData);
    priceDao.save(priceData2);

    List<String> symbols = priceDao.findAllSymbols();
    assertEquals(symbols.size(), 2);
    assertTrue(symbols.contains("MEG"));
    assertTrue(symbols.contains("BDO"));
  }

  @Test
  public void testSort() {
    // Prepare data
    Price price1 = new Price(1.1, 1.2, 1.0, 1.1, 10000, now);
    Price price2 = new Price(2.1, 2.2, 2.0, 2.1, 20000, now.plusDays(1));

    List<Price> prices = Lists.newArrayList(price2, price1);

    // Save Price data
    priceDao.save("MEG", prices);

    // Find data
    Optional<PriceData> foundData = priceDao.findByYear("MEG", 2016);
    PriceData resultData = foundData.get();

    // Verify Price 1
    TestUtil.assertPriceEquals(resultData.getData().get(0), price1);

    // Verify Price 2
    TestUtil.assertPriceEquals(resultData.getData().get(1), price2);
  }

  @Test
  public void testSaveWithOverlap() {
    // Set 1
    Price price1 = new Price(1.1, 1.2, 1.0, 1.1, 10000, now);
    Price price2 = new Price(2.1, 2.2, 2.0, 2.1, 20000, now.plusDays(1));
    Price price3 = new Price(3.1, 3.2, 3.3, 3.4, 30000, now.plusDays(2));

    // Set 2
    Price price4 = new Price(4.1, 4.2, 4.3, 4.4, 40000, now.plusDays(2));
    Price price5 = new Price(5.1, 5.2, 5.3, 5.4, 50000, now.plusDays(3));

    // Save set 1 prices
    priceDao.save("MEG", Lists.newArrayList(price3, price2, price1));

    // Find data
    Optional<PriceData> foundData = priceDao.findByYear("MEG", 2016);
    PriceData resultData = foundData.get();
    assertEquals(resultData.getData().size(), 3);

    // Verify data is sorted
    TestUtil.assertPriceEquals(resultData.getData().get(0), price1);
    TestUtil.assertPriceEquals(resultData.getData().get(1), price2);
    TestUtil.assertPriceEquals(resultData.getData().get(2), price3);

    // Save set 2 prices. price4 overwrites price3
    priceDao.save("MEG", Lists.newArrayList(price1, price4, price5));

    // Find data
    foundData = priceDao.findByYear("MEG", 2016);
    resultData = foundData.get();
    assertEquals(resultData.getData().size(), 4);
    TestUtil.assertPriceEquals(resultData.getData().get(2), price4);
  }

  @Test
  public void testSaveMultipleYears() {
    // Year 1
    LocalDate date1 = LocalDate.of(2014, 1, 1);
    Price price1 = new Price(1.1, 1.2, 1.0, 1.1, 10000, date1);

    // Year 2
    LocalDate date2 = LocalDate.of(2015, 1, 1);
    Price price2 = new Price(2.1, 2.2, 2.0, 2.1, 20000, date2);
    Price price3 = new Price(3.1, 3.2, 3.3, 3.4, 30000, date2.plusDays(1));

    // Year 3
    LocalDate date3 = LocalDate.of(2016, 12, 29);
    Price price4 = new Price(4.1, 4.2, 4.3, 4.4, 40000, date3);
    Price price5 = new Price(5.1, 5.2, 5.3, 5.4, 50000, date3.plusDays(1));
    Price price6 = new Price(6.1, 6.2, 6.3, 6.4, 60000, date3.plusDays(2));

    // Add in any order
    List<Price> prices = Lists.newArrayList(price5, price3, price4, price2, price6, price1);

    priceDao.save("MEG", prices);

    // Verify Year 1
    Optional<PriceData> foundData = priceDao.findByYear("MEG", 2014);
    assertTrue(foundData.isPresent());
    assertEquals(foundData.get().getData().size(), 1);
    TestUtil.assertPriceEquals(foundData.get().getData().get(0), price1);

    // Verify Year 2
    foundData = priceDao.findByYear("MEG", 2015);
    assertTrue(foundData.isPresent());
    assertEquals(foundData.get().getData().size(), 2);
    TestUtil.assertPriceEquals(foundData.get().getData().get(0), price2);
    TestUtil.assertPriceEquals(foundData.get().getData().get(1), price3);

    // Verify Year 3
    foundData = priceDao.findByYear("MEG", 2016);
    assertTrue(foundData.isPresent());
    assertEquals(foundData.get().getData().size(), 3);
    TestUtil.assertPriceEquals(foundData.get().getData().get(0), price4);
    TestUtil.assertPriceEquals(foundData.get().getData().get(1), price5);
    TestUtil.assertPriceEquals(foundData.get().getData().get(2), price6);
  }

  @Test
  public void testRemoveByDates() {
    // Year 1
    LocalDate date1 = LocalDate.of(2014, 1, 1);
    Price price1 = new Price(1.1, 1.2, 1.0, 1.1, 10000, date1);

    // Year 2
    LocalDate date2 = LocalDate.of(2015, 1, 1);
    Price price2 = new Price(2.1, 2.2, 2.0, 2.1, 20000, date2);
    Price price3 = new Price(3.1, 3.2, 3.3, 3.4, 30000, date2.plusDays(1));

    // Year 3
    LocalDate date3 = LocalDate.of(2016, 12, 29);
    Price price4 = new Price(4.1, 4.2, 4.3, 4.4, 40000, date3);
    Price price5 = new Price(5.1, 5.2, 5.3, 5.4, 50000, date3.plusDays(1));
    Price price6 = new Price(6.1, 6.2, 6.3, 6.4, 60000, date3.plusDays(2));

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
    Price price1 = new Price(1.1, 1.2, 1.0, 1.1, 10000, date1);
    Price price2 = new Price(2.1, 2.2, 2.0, 2.1, 20000, date1);
    Price price3 = new Price(3.1, 3.2, 3.3, 3.4, 30000, date1);
    Price price4 = new Price(4.1, 4.2, 4.3, 4.4, 40000, date1.plusDays(1));

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
}
