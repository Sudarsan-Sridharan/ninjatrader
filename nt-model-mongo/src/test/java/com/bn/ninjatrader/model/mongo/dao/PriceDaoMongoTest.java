package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.mongo.factory.PriceBuilderFactoryMongo;
import com.bn.ninjatrader.model.mongo.guice.NtModelTestModule;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.model.request.FindPriceRequest;
import com.bn.ninjatrader.model.request.SavePriceRequest;
import com.bn.ninjatrader.model.util.TestUtil;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 5/4/16.
 */
public class PriceDaoMongoTest {
  private static final Logger LOG = LoggerFactory.getLogger(PriceDaoMongoTest.class);

  private static final LocalDate now = LocalDate.of(2016, 1, 1);
  private static final LocalDate tomorrow = now.plusDays(1);
  private static final LocalDate nextMonth = now.plusMonths(1);
  private static final LocalDate nextYear = now.plusYears(1);

  private static Injector injector;

  private PriceBuilderFactory pbf = new PriceBuilderFactoryMongo();
  private final Price price1 = pbf.builder().date(now)
      .open(1.1).high(1.2).low(1.0).close(1.1).volume(1000).build();
  private final Price price2 = pbf.builder().date(tomorrow)
      .open(2.1).high(2.2).low(2.0).close(2.1).volume(2000).build();

  private PriceDaoMongo priceDao;

  @BeforeClass
  public static void setup() {
    injector = Guice.createInjector(Modules
        .override(new NtModelTestModule())
        .with(new AbstractModule() {
          @Override
          protected void configure() {
            bind(Clock.class).toInstance(TestUtil.fixedClock(now));
          }
        })
    );
  }

  @Before
  public void before() {
    priceDao = injector.getInstance(PriceDaoMongo.class);
    priceDao.getMongoCollection().remove();
  }

  @Test
  public void testFindByDateRange_shouldRetrievePricesWithinDateRange() {
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(Lists.newArrayList(price1, price2)));

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(now))).containsExactly(price1);
    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(tomorrow).to(tomorrow))).containsExactly(price2);
    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(tomorrow))).containsExactly(price1, price2);
    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(nextYear))).containsExactly(price1, price2);
    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(nextYear).to(nextYear))).isEmpty();
  }

  @Test
  public void testFindBySymbol_shouldRetrievePricesWithMatchingSymbol() {
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(Lists.newArrayList(price1)));
    priceDao.save(SavePriceRequest.forSymbol("BDO").addPrices(Lists.newArrayList(price2)));

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(tomorrow))).containsExactly(price1);
    assertThat(priceDao.find(FindPriceRequest.forSymbol("BDO").from(now).to(tomorrow))).containsExactly(price2);
  }

  @Test
  public void testFindAllSymbols_shouldReturnAllSymbols() {
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(Lists.newArrayList(price1)));
    priceDao.save(SavePriceRequest.forSymbol("BDO").addPrices(Lists.newArrayList(price2)));

    assertThat(priceDao.findAllSymbols()).containsOnly("MEG", "BDO");
  }

  @Test
  public void testSort_shouldSortPricesByDate() {
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(Lists.newArrayList(price2, price1)));
    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(now.plusDays(1)))).containsExactly(price1, price2);
  }

  @Test
  public void testSaveWithOverlap_shouldOverwriteExistingPrices() {
    // Set 1
    final Price price1 = pbf.builder().date(now).open(1.1).high(1.2).low(1.0).close(1.1).volume(1000).build();
    final Price price2 = pbf.builder().date(tomorrow).open(2.1).high(2.2).low(2.0).close(2.1).volume(2000).build();
    final Price price3 = pbf.builder().date(now.plusDays(2))
        .open(3.1).high(3.2).low(3.3).close(3.4).volume(3000).build();

    // Set 2
    final Price price4 = pbf.builder().date(now.plusDays(2))
        .open(4.1).high(4.2).low(4.3).close(4.4).volume(4000).build();
    final Price price5 = pbf.builder().date(now.plusDays(3))
        .open(5.1).high(5.2).low(5.3).close(5.4).volume(5000).build();

    // Save set 1 prices
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price3, price2, price1));

    // Save set 2 prices. price4 overwrites price3
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price1, price4, price5));

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(now).to(now.plusDays(3))))
        .containsExactly(price1, price2, price4, price5);
  }

  @Test
  public void testRemoveByDates_shouldRemovePricesWithGivenDates() {
    // Year 2014
    final LocalDate year2014 = LocalDate.of(2014, 1, 1);
    final Price price1 = pbf.builder().date(year2014).open(1.1).high(1.2).low(1.0).close(1.1).build();

    // Year 2015
    final LocalDate year2015 = LocalDate.of(2015, 1, 1);
    final LocalDate nextDay2015 = year2015.plusDays(1);
    final Price price2 = pbf.builder().date(year2015).open(2.1).high(2.2).low(2.0).close(2.1).build();
    final Price price3 = pbf.builder().date(nextDay2015).open(3.1).high(3.2).low(3.3).close(3.4).build();

    // Year 2016
    final LocalDate year2016 = LocalDate.of(2016, 12, 29);
    final LocalDate nextDay2016 = year2016.plusDays(1);
    final LocalDate next2Days2016 = year2016.plusDays(2);
    final Price price4 = pbf.builder().date(year2016).open(4.1).high(4.2).low(4.3).close(4.4).build();
    final Price price5 = pbf.builder().date(nextDay2016).open(5.1).high(5.2).low(5.3).close(5.4).build();
    final Price price6 = pbf.builder().date(next2Days2016).open(6.1).high(6.2).low(6.3).close(6.4).build();

    // Save prices
    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(price1, price2, price3, price4, price5, price6));

    // Remove by dates
    final List<LocalDate> removeDates = Lists.newArrayList(price2.getDate(), price6.getDate(), price1.getDate());
    priceDao.removeByDates(SavePriceRequest.forSymbol("MEG"), removeDates);

    // Verify results. 3 removed, 3 remaining
    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").from(year2014).to(price6.getDate())))
        .containsExactly(price3, price4, price5);
  }

  @Test
  public void testSimpleFindNBarsBeforeDate_shouldReturnPricesBeforeGivenDate() {
    final Price priceNow = pbf.builder().date(now).close(1).build();
    final Price priceTomorrow = pbf.builder().date(tomorrow).close(2).build();
    final Price priceNextMonth = pbf.builder().date(nextMonth).close(3).build();

    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(priceNow, priceTomorrow, priceNextMonth));

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

    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(dbPrices));

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

    priceDao.save(SavePriceRequest.forSymbol("MEG").addPrices(prices));

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
    priceDao.save(SavePriceRequest.forSymbol("MEG").timeframe(TimeFrame.ONE_DAY).addPrices(price1));
    priceDao.save(SavePriceRequest.forSymbol("MEG").timeframe(TimeFrame.ONE_WEEK).addPrices(price2));

    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").timeframe(TimeFrame.ONE_DAY).from(now).to(now.plusDays(1))))
        .containsExactly(price1);
    assertThat(priceDao.find(FindPriceRequest.forSymbol("MEG").timeframe(TimeFrame.ONE_WEEK).from(now).to(now.plusDays(1))))
        .containsExactly(price2);
  }

  private Price lastPriceOf(List<Price> prices) {
    return prices.get(prices.size() - 1);
  }
}
