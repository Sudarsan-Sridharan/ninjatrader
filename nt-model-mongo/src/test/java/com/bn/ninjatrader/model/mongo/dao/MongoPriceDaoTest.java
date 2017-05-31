package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.mongo.factory.PriceBuilderFactoryMongo;
import com.bn.ninjatrader.model.mongo.guice.NtModelTestModule;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.model.util.TestUtil;
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
public class MongoPriceDaoTest {
  private static final Logger LOG = LoggerFactory.getLogger(MongoPriceDaoTest.class);

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
  private final Price priceNextMonth = pbf.builder().date(nextMonth)
      .open(3.1).high(3.2).low(3.0).close(3.1).volume(3000).build();
  private final Price priceNextYear = pbf.builder().date(nextYear)
      .open(4.1).high(4.2).low(4.0).close(4.1).volume(4000).build();

  private MongoPriceDao priceDao;

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
    priceDao = injector.getInstance(MongoPriceDao.class);
    priceDao.getMongoCollection().remove();
  }

  @Test
  public void testFindByDateRange_shouldRetrievePricesWithinDateRange() {
    priceDao.savePrices(price1, price2).withSymbol("MEG").now();
    
    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(now).now()).containsExactly(price1);
    assertThat(priceDao.findPrices().withSymbol("MEG").from(tomorrow).to(tomorrow).now()).containsExactly(price2);
    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(tomorrow).now()).containsExactly(price1, price2);
    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(nextYear).now()).containsExactly(price1, price2);
    assertThat(priceDao.findPrices().withSymbol("MEG").from(nextYear).to(nextYear).now()).isEmpty();
  }

  @Test
  public void testFindBySymbol_shouldRetrievePricesWithMatchingSymbol() {
    priceDao.savePrices(price1).withSymbol("MEG").now();
    priceDao.savePrices(price2).withSymbol("BDO").now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(tomorrow).now()).containsExactly(price1);
    assertThat(priceDao.findPrices().withSymbol("BDO").from(now).to(tomorrow).now()).containsExactly(price2);
  }

  @Test
  public void testFindAllSymbols_shouldReturnAllSymbols() {
    priceDao.savePrices(price1).withSymbol("MEG").now();
    priceDao.savePrices(price2).withSymbol("BDO").now();

    assertThat(priceDao.findAllSymbols()).containsOnly("MEG", "BDO");
  }

  @Test
  public void testSort_shouldSortPricesByDate() {
    priceDao.savePrices(price2, price1).withSymbol("MEG").now();
    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(now.plusDays(1)).now())
        .containsExactly(price1, price2);
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
    priceDao.savePrices(price3, price2, price1).withSymbol("MEG").now();

    // Save set 2 prices. price4 overwrites price3
    priceDao.savePrices(price1, price4, price5).withSymbol("MEG").now();

    assertThat(priceDao.findPrices().withSymbol("MEG").from(now).to(now.plusDays(3)).now())
        .containsExactly(price1, price2, price4, price5);
  }

  @Test
  public void testSimpleFindNBarsBeforeDate_shouldReturnPricesBeforeGivenDate() {
    final Price priceNow = pbf.builder().date(now).close(1).build();
    final Price priceTomorrow = pbf.builder().date(tomorrow).close(2).build();
    final Price priceNextMonth = pbf.builder().date(nextMonth).close(3).build();

    priceDao.savePrices(priceNow, priceTomorrow, priceNextMonth).withSymbol("MEG").now();

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

    priceDao.savePrices(dbPrices).withSymbol("MEG").now();

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

    priceDao.savePrices(prices).withSymbol("MEG").now();

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
    priceDao.savePrices(price1).withSymbol("MEG").withTimeFrame(TimeFrame.ONE_DAY).now();
    priceDao.savePrices(price2).withSymbol("MEG").withTimeFrame(TimeFrame.ONE_WEEK).now();

    assertThat(priceDao.findPrices().withSymbol("MEG").withTimeFrame(TimeFrame.ONE_DAY)
        .from(now).to(now.plusDays(1)).now())
        .containsExactly(price1);
    assertThat(priceDao.findPrices().withSymbol("MEG").withTimeFrame(TimeFrame.ONE_WEEK)
        .from(now).to(now.plusDays(1)).now())
        .containsExactly(price2);
  }

  @Test
  public void testRenameSymbol_shouldMergePricesAndRenameToNewSymbol() {
    // Add prices for different time frames to old symbol
    priceDao.savePrices(price1, priceNextYear).withSymbol("MEG").withTimeFrame(TimeFrame.ONE_DAY).now();
    priceDao.savePrices(price2).withSymbol("MEG").withTimeFrame(TimeFrame.ONE_WEEK).now();

    // Add prices for different time frames to new symbol
    priceDao.savePrices(priceNextMonth).withSymbol("LEG").withTimeFrame(TimeFrame.ONE_DAY).now();
    priceDao.savePrices(priceNextMonth).withSymbol("LEG").withTimeFrame(TimeFrame.ONE_WEEK).now();

    // Rename
    priceDao.renameSymbol("MEG").to("LEG").now();

    // Verify that prices for MEG no longer exists
    assertThat(priceDao.findPrices().withSymbol("MEG").withTimeFrame(TimeFrame.ONE_DAY)
        .from(now).to(nextYear).now()).isEmpty();
    assertThat(priceDao.findPrices().withSymbol("MEG").withTimeFrame(TimeFrame.ONE_WEEK)
        .from(now).to(nextYear).now()).isEmpty();

    // Verify prices of old symbol are merged to new symbol
    assertThat(priceDao.findPrices().withSymbol("LEG").withTimeFrame(TimeFrame.ONE_DAY)
        .from(now).to(nextYear).now())
        .containsExactly(price1, priceNextMonth, priceNextYear);
    assertThat(priceDao.findPrices().withSymbol("LEG").withTimeFrame(TimeFrame.ONE_WEEK)
        .from(now).to(nextYear).now())
        .containsExactly(price2, priceNextMonth);
  }

  private Price lastPriceOf(List<Price> prices) {
    return prices.get(prices.size() - 1);
  }
}
