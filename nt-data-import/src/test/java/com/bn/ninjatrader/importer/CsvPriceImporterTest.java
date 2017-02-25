package com.bn.ninjatrader.importer;

import com.bn.ninjatrader.dataimport.history.CsvPriceImporter;
import com.bn.ninjatrader.dataimport.history.parser.CsvDataParser;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.DailyQuote;
import com.bn.ninjatrader.model.entity.PriceBuilder;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.request.SavePriceRequest;
import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Brad on 6/6/16.
 */
public class CsvPriceImporterTest {

  private static final Logger LOG = LoggerFactory.getLogger(CsvPriceImporterTest.class);

  private CsvDataParser parser;
  private PriceDao priceDao;
  private PriceBuilderFactory priceBuilderFactory;
  private PriceBuilder priceBuilder;

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
    priceBuilderFactory = new DummyPriceBuilderFactory();

    importer = new CsvPriceImporter(parser, priceDao, priceBuilderFactory);
  }

  @Test
  public void testSaveDifferentSymbols_shouldSaveQuotesForEachSymbol() {
    final ArgumentCaptor<SavePriceRequest> requestCaptor = ArgumentCaptor.forClass(SavePriceRequest.class);
    importer.save(Lists.newArrayList(quote1, quote2, quote3, quote4, quote5));

    verify(priceDao, times(2)).save(requestCaptor.capture());

    final SavePriceRequest saveBdoRequest = requestCaptor.getAllValues().get(0);

    assertThat(saveBdoRequest.getSymbol()).isEqualTo("BDO");
    assertThat(saveBdoRequest.getPrices())
        .containsExactly(
            quote3.getPrice(priceBuilderFactory),
            quote4.getPrice(priceBuilderFactory));

    final SavePriceRequest saveMegRequest = requestCaptor.getAllValues().get(1);
    assertThat(saveMegRequest.getSymbol()).isEqualTo("MEG");
    assertThat(saveMegRequest.getPrices())
        .containsExactly(
            quote1.getPrice(priceBuilderFactory),
            quote2.getPrice(priceBuilderFactory),
            quote5.getPrice(priceBuilderFactory));
  }
}
