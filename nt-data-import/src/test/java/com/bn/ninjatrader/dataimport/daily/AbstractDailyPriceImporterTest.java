package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.DailyQuote;
import com.bn.ninjatrader.model.entity.Price;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class AbstractDailyPriceImporterTest {

  private final LocalDate now = LocalDate.of(2017, 5, 1); // Monday
  private final LocalDate tomorrow = LocalDate.of(2017, 5, 2); // Tuesday
  private final LocalDate weekend = LocalDate.of(2017, 5, 6);

  private AbstractDailyPriceImporter importer;
  private PriceDao priceDao;
  private DailyQuote megQuote = new DailyQuote("MEG", now, 1, 1, 1, 1, 1000);

  @Before
  public void before() {
    priceDao = mock(PriceDao.class);

    when(priceDao.savePrices(any(Price.class))).thenReturn(mock(PriceDao.SavePricesOperation.class, RETURNS_SELF));

    importer = new AbstractDailyPriceImporter(priceDao) {
      @Override
      protected List<DailyQuote> provideDailyQuotes(LocalDate date) {
        return Lists.newArrayList(megQuote);
      }
    };
  }

  @Test
  public void testImportData_shouldImportDataForGivenDate() {
    importer.importData(now);

    verify(priceDao).savePrices(megQuote.getPrice());
  }

  @Test
  public void testImportDataForMultiDates_shouldImportDataForAllGivenDate() {
    importer.importData(Lists.newArrayList(now, tomorrow));

    verify(priceDao, times(2)).savePrices(megQuote.getPrice());
  }

  @Test
  public void testImportData_shouldReturnImportedDataForGivenDate() {
    final List<DailyQuote> quotes = importer.importData(Lists.newArrayList(now));

    assertThat(quotes).isNotNull();
    assertThat(quotes).containsExactly(megQuote);
  }

  @Test
  public void testImporDataOnWeekend_shouldNotImportData() {
    importer.importData(weekend);

    verify(priceDao, times(0)).savePrices(any());
  }
}
