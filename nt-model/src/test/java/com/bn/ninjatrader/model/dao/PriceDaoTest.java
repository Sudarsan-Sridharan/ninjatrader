package com.bn.ninjatrader.model.dao;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.document.PriceDoc;
import com.bn.ninjatrader.model.guice.NtModelTestModule;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 5/4/16.
 */
public class PriceDaoTest extends AbstractDaoTest {
  private static final Logger LOG = LoggerFactory.getLogger(PriceDaoTest.class);

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final LocalDate tomorrow = now.plusDays(1);
  private final LocalDate nextMonth = now.plusMonths(1);
  private final LocalDate nextYear = now.plusYears(1);

  private final Price price1 = Price.builder().date(now)
      .open(1.1).high(1.2).low(1.0).close(1.1).volume(1000).build();
  private final Price price2 = Price.builder().date(tomorrow)
      .open(2.1).high(2.2).low(2.0).close(2.1).volume(2000).build();

  private PriceDao priceDao;

  @BeforeClass
  public void setup() {
    final Injector injector = Guice.createInjector(new NtModelTestModule());
    priceDao = injector.getInstance(PriceDao.class);
  }

  @BeforeMethod
  public void cleanup() {
    final MongoCollection collection = priceDao.getMongoCollection();
    collection.remove();
  }

  @Test
  public void testSaveAndFind_shouldSaveAndRetrieveEqualObject() {
    // Prepare document
    final PriceDoc priceData = new PriceDoc("MEG", 2016);

    // Add Price document for January 1 and 2, 2016
    priceData.getData().add(price1);
    priceData.getData().add(price2);

    // Save Price document
    priceDao.save(priceData);

    // Find document
    final List<PriceDoc> result = priceDao.find();
    assertThat(result).isNotNull().hasSize(1);

    // Verify document
    final PriceDoc resultData = result.get(0);
    assertThat(resultData.getSymbol()).isEqualTo(priceData.getSymbol());
    assertThat(resultData.getYear()).isEqualTo(priceData.getYear());
    assertThat(resultData.getData()).containsExactly(price1, price2);
  }

  @Test
  public void testFindByDateRange_shouldRetrievePricesWithinDateRange() {
    priceDao.save(SaveRequest.save("MEG").values(Lists.newArrayList(price1, price2)));

    assertThat(priceDao.find(findSymbol("MEG").from(now).to(now))).containsExactly(price1);
    assertThat(priceDao.find(findSymbol("MEG").from(tomorrow).to(tomorrow))).containsExactly(price2);
    assertThat(priceDao.find(findSymbol("MEG").from(now).to(tomorrow))).containsExactly(price1, price2);
    assertThat(priceDao.find(findSymbol("MEG").from(now).to(nextYear))).containsExactly(price1, price2);
    assertThat(priceDao.find(findSymbol("MEG").from(nextYear).to(nextYear))).isEmpty();
  }

  @Test
  public void testFindBySymbol_shouldRetrievePricesWithMatchingSymbol() {
    priceDao.save(SaveRequest.save("MEG").values(Lists.newArrayList(price1)));
    priceDao.save(SaveRequest.save("BDO").values(Lists.newArrayList(price2)));

    assertThat(priceDao.find(findSymbol("MEG").from(now).to(tomorrow))).containsExactly(price1);
    assertThat(priceDao.find(findSymbol("BDO").from(now).to(tomorrow))).containsExactly(price2);
  }

  //TODO
//  @Test
//  public void testFindAllSymbols_shouldReturnAllSymbols() {
//    priceDao.save(SaveRequest.save("MEG").values(Lists.newArrayList(price1)));
//    priceDao.save(SaveRequest.save("BDO").values(Lists.newArrayList(price2)));
//
//    assertThat(priceDao.findAllSymbols()).hasSize(2).containsOnly("MEG", "BDO");
//  }

  @Test
  public void testSort_shouldSortPricesByDate() {
    priceDao.save(SaveRequest.save("MEG").values(Lists.newArrayList(price2, price1)));
    assertThat(priceDao.find(findSymbol("MEG").from(now).to(now.plusDays(1)))).containsExactly(price1, price2);
  }

  @Test
  public void testSaveWithOverlap_shouldOverwriteExistingPrices() {
    // Set 1
    final Price price1 = Price.builder().date(now).open(1.1).high(1.2).low(1.0).close(1.1).volume(1000).build();
    final Price price2 = Price.builder().date(tomorrow).open(2.1).high(2.2).low(2.0).close(2.1).volume(2000).build();
    final Price price3 = Price.builder().date(now.plusDays(2))
        .open(3.1).high(3.2).low(3.3).close(3.4).volume(3000).build();

    // Set 2
    final Price price4 = Price.builder().date(now.plusDays(2))
        .open(4.1).high(4.2).low(4.3).close(4.4).volume(4000).build();
    final Price price5 = Price.builder().date(now.plusDays(3))
        .open(5.1).high(5.2).low(5.3).close(5.4).volume(5000).build();

    // Save set 1 prices
    priceDao.save(SaveRequest.save("MEG").values(price3, price2, price1));

    // Save set 2 prices. price4 overwrites price3
    priceDao.save(SaveRequest.save("MEG").values(price1, price4, price5));

    assertThat(priceDao.find(findSymbol("MEG").from(now).to(now.plusDays(3))))
        .containsExactly(price1, price2, price4, price5);
  }

  @Test
  public void testRemoveByDates_shouldRemovePricesWithGivenDates() {
    // Year 2014
    final LocalDate year2014 = LocalDate.of(2014, 1, 1);
    final Price price1 = Price.builder().date(year2014).open(1.1).high(1.2).low(1.0).close(1.1).build();

    // Year 2015
    final LocalDate year2015 = LocalDate.of(2015, 1, 1);
    final LocalDate nextDay2015 = year2015.plusDays(1);
    final Price price2 = Price.builder().date(year2015).open(2.1).high(2.2).low(2.0).close(2.1).build();
    final Price price3 = Price.builder().date(nextDay2015).open(3.1).high(3.2).low(3.3).close(3.4).build();

    // Year 2016
    final LocalDate year2016 = LocalDate.of(2016, 12, 29);
    final LocalDate nextDay2016 = year2016.plusDays(1);
    final LocalDate next2Days2016 = year2016.plusDays(2);
    final Price price4 = Price.builder().date(year2016).open(4.1).high(4.2).low(4.3).close(4.4).build();
    final Price price5 = Price.builder().date(nextDay2016).open(5.1).high(5.2).low(5.3).close(5.4).build();
    final Price price6 = Price.builder().date(next2Days2016).open(6.1).high(6.2).low(6.3).close(6.4).build();

    // Save prices
    priceDao.save(SaveRequest.save("MEG").values(price1, price2, price3, price4, price5, price6));

    // Remove by dates
    final List<LocalDate> removeDates = Lists.newArrayList(price2.getDate(), price6.getDate(), price1.getDate());
    priceDao.removeByDates(SaveRequest.save("MEG"), removeDates);

    // Verify results. 3 removed, 3 remaining
    assertThat(priceDao.find(findSymbol("MEG").from(year2014).to(price6.getDate())))
        .containsExactly(price3, price4, price5);
  }

  @Test
  public void testRemoveByDatesForAllSymbols_shouldRemoveAllPricesForAllSymbolsWithGivenDates() {
    final Price price1 = Price.builder().date(now).open(1.1).high(1.2).low(1.0).close(1.1).volume(1000).build();
    final Price price2 = Price.builder().date(now).open(2.1).high(2.2).low(2.0).close(2.1).volume(2000).build();
    final Price price3 = Price.builder().date(now).open(3.1).high(3.2).low(3.3).close(3.4).volume(3000).build();
    final Price price4 = Price.builder().date(tomorrow).open(4.1).high(4.2).low(4.3).close(4.4).volume(4000).build();

    priceDao.save(SaveRequest.save("MEG").values(price1));
    priceDao.save(SaveRequest.save("BDO").values(price2));
    priceDao.save(SaveRequest.save("MBT").values(price3));
    priceDao.save(SaveRequest.save("MEG").values(price4)); // Should remain

    priceDao.removeByDates(Lists.newArrayList(now));

    List<PriceDoc> results = priceDao.find();
    assertEquals(results.size(), 3);

    assertThat(priceDao.find()).hasSize(3); // Should still have 3 documents with no prices.
    assertThat(priceDao.find(findSymbol("BDO").from(now).to(now))).isEmpty();
    assertThat(priceDao.find(findSymbol("MBT").from(now).to(now))).isEmpty();
    assertThat(priceDao.find(findSymbol("MEG").from(now).to(tomorrow))).containsExactly(price4);
  }

  @Test
  public void testSimpleFindNBarsBeforeDate_shouldReturnPricesBeforeGivenDate() {
    final Price priceNow = Price.builder().date(now).close(1).build();
    final Price priceTomorrow = Price.builder().date(tomorrow).close(2).build();
    final Price priceNextMonth = Price.builder().date(nextMonth).close(3).build();

    priceDao.save(SaveRequest.save("MEG").values(priceNow, priceTomorrow, priceNextMonth));

    assertThat(priceDao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(1).beforeDate(now).build()))
        .isEmpty();
    assertThat(priceDao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(1).beforeDate(tomorrow).build()))
        .containsExactly(priceNow);
    assertThat(priceDao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG").timeFrame(TimeFrame.ONE_DAY).numOfValues(1).beforeDate(nextMonth).build()))
        .containsExactly(priceTomorrow);
  }

  @Test
  public void testFindNBarsBeforeDateWithLotsOfBars_shouldReturnPricesBeforeGivenDate() {
    final List<Price> dbPrices = TestUtil.randomPricesForDateRange(now.minusYears(1), now);

    priceDao.save(SaveRequest.save("MEG").values(dbPrices));

    // Test with 30 bars
    List<Price> result = priceDao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG")
        .timeFrame(TimeFrame.ONE_DAY)
        .numOfValues(30)
        .beforeDate(now)
        .build());

    assertThat(result).hasSize(30).endsWith(dbPrices.get(dbPrices.size() - 2));
    result.forEach(price -> {
      assertThat(price.getDate()).isBeforeOrEqualTo(now);
    });

    // Test with past date
    LocalDate pastDate = LocalDate.of(2015, 12, 30);
    result = priceDao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG")
        .timeFrame(TimeFrame.ONE_DAY)
        .numOfValues(30)
        .beforeDate(pastDate)
        .build());

    assertThat(result).hasSize(30);
    assertThat(lastPriceOf(result).getDate()).isEqualTo(LocalDate.of(2015, 12, 29));
    result.forEach(price -> {
      assertThat(price.getDate()).isBeforeOrEqualTo(pastDate);
    });
  }

  @Test
  public void testFindNBarsBeforeDateWithMultipleYears_shouldReturnPricesBeforeGivenDate() {
    final List<Price> prices = TestUtil.randomPricesForDateRange(now.minusYears(2), now);

    priceDao.save(SaveRequest.save("MEG").values(prices));

    // Test with 400 bars
    final List<Price> result = priceDao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol("MEG")
        .timeFrame(TimeFrame.ONE_DAY)
        .numOfValues(400)
        .beforeDate(now)
        .build());

    assertThat(result).hasSize(400).endsWith(prices.get(prices.size() - 2));
    assertThat(result.get(0).getDate()).isEqualTo(LocalDate.of(2014, 6, 20));
  }

  @Test
  public void testSaveFindWithMultipleTimeFrames_shouldSaveOnGivenTimeframe() {
    priceDao.save(SaveRequest.save("MEG").timeFrame(TimeFrame.ONE_DAY).values(price1));
    priceDao.save(SaveRequest.save("MEG").timeFrame(TimeFrame.ONE_WEEK).values(price2));

    assertThat(priceDao.find(findSymbol("MEG").timeframe(TimeFrame.ONE_DAY).from(now).to(now.plusDays(1))))
        .containsExactly(price1);
    assertThat(priceDao.find(findSymbol("MEG").timeframe(TimeFrame.ONE_WEEK).from(now).to(now.plusDays(1))))
        .containsExactly(price2);
  }

  private Price lastPriceOf(List<Price> prices) {
    return prices.get(prices.size() - 1);
  }
}
