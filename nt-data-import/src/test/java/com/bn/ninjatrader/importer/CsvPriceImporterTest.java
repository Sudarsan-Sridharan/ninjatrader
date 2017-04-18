package com.bn.ninjatrader.importer;

import com.bn.ninjatrader.dataimport.history.CsvPriceImporter;
import com.bn.ninjatrader.dataimport.history.parser.CsvDataParser;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.DailyQuote;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Brad on 6/6/16.
 */
public class CsvPriceImporterTest {

  private static final Logger LOG = LoggerFactory.getLogger(CsvPriceImporterTest.class);

  private CsvDataParser parser;
  private PriceDao priceDao;
  private PriceBuilderFactory priceFactory;

  private CsvPriceImporter importer;

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final LocalDate tomorrow = now.plusDays(1);
  private final LocalDate lastYear = now.minusYears(1);

  private final DailyQuote quote1 = new DailyQuote("MEG", now, 1.1, 1.2, 1.3, 1.4, 1000);
  private final DailyQuote quote2 = new DailyQuote("MEG", tomorrow, 1.2, 1.3, 1.4, 1.5, 1001);
  private final DailyQuote quote3 = new DailyQuote("BDO", now, 2.1, 2.2, 2.3, 2.4, 2000);
  private final DailyQuote quote4 = new DailyQuote("BDO", tomorrow, 2.2, 2.3, 2.4, 2.5, 2001);
  private final DailyQuote quote5 = new DailyQuote("MEG", lastYear, 1.3, 1.4, 1.5, 1.6, 1002);

  @Before
  public void before() {
    parser = mock(CsvDataParser.class);
    priceDao = mock(PriceDao.class);
    priceFactory = new DummyPriceBuilderFactory();

    when(priceDao.savePrices(any())).thenReturn(mock(PriceDao.SavePricesOperation.class, RETURNS_SELF));

    importer = new CsvPriceImporter(parser, priceDao, priceFactory);
  }

  @Test
  public void testSaveDifferentSymbols_shouldSaveQuotesForEachSymbol() {
    importer.save(Lists.newArrayList(quote1, quote2, quote3, quote4, quote5));

    final List<Price> bdoExpectedPrices = Lists.newArrayList(quote3.getPrice(priceFactory),
        quote4.getPrice(priceFactory));

    final List<Price> megExpectedPrices = Lists.newArrayList(quote1.getPrice(priceFactory),
        quote2.getPrice(priceFactory), quote5.getPrice(priceFactory));

    verify(priceDao.savePrices(bdoExpectedPrices)).withSymbol("BDO");

    verify(priceDao.savePrices(megExpectedPrices)).withSymbol("MEG");
  }
}
