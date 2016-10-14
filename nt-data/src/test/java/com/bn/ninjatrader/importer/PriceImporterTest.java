package com.bn.ninjatrader.importer;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.data.history.PriceImporter;
import com.bn.ninjatrader.data.history.parser.CsvDataParser;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.SaveRequest;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Created by Brad on 6/6/16.
 */
public class PriceImporterTest {

  private static final Logger log = LoggerFactory.getLogger(PriceImporterTest.class);

  @Mocked
  @Injectable
  private CsvDataParser parser;

  @Mocked
  @Injectable
  private PriceDao priceDao;

  @Tested
  private PriceImporter importer;

  private LocalDate date1 = LocalDate.of(2016, 1, 1);
  private LocalDate date2 = LocalDate.of(2016, 1, 2);
  private LocalDate date3 = LocalDate.of(2015, 1, 1);

  private DailyQuote quote1 = new DailyQuote("MEG", date1, 1.1, 1.2, 1.3, 1.4, 1000);
  private DailyQuote quote2 = new DailyQuote("MEG", date2, 1.2, 1.3, 1.4, 1.5, 1001);
  private DailyQuote quote3 = new DailyQuote("BDO", date1, 2.1, 2.2, 2.3, 2.4, 2000);
  private DailyQuote quote4 = new DailyQuote("BDO", date2, 2.2, 2.3, 2.4, 2.5, 2001);
  private DailyQuote quote5 = new DailyQuote("MEG", date3, 1.3, 1.4, 1.5, 1.6, 1002);

  @Test
  public void testSave() {
    List<DailyQuote> quotes = Lists.newArrayList();

    // Add Stock 1
    quotes.add(quote1);
    quotes.add(quote2);

    // Add Stock 2
    quotes.add(quote3);
    quotes.add(quote4);

    // Add Stock 1 but with different year
    quotes.add(quote5);

    importer.save(quotes);

    new Verifications() {{
      List<SaveRequest<Price>> saveRequests = Lists.newArrayList();

      // Must be called 3 times
      priceDao.save(withCapture(saveRequests)); times = 2;

      // Verify each parameter
      int successCode = verifySavePriceParameters(saveRequests.get(0));
      successCode += verifySavePriceParameters(saveRequests.get(1));

      assertEquals(successCode, 11);
    }};
  }

  /**
   * Verify that prices are organized according to their symbols.
   */
  private int verifySavePriceParameters(SaveRequest<Price> req) {
    if ("MEG".equals(req.getSymbol())) {
      assertEquals(req.getValues().size(), 3);
      TestUtil.assertPriceEqualsQuote(req.getValues().get(0), quote1);
      TestUtil.assertPriceEqualsQuote(req.getValues().get(1), quote2);
      TestUtil.assertPriceEqualsQuote(req.getValues().get(2), quote5);
      return 1;

    } else if ("BDO".equals(req.getSymbol())) {
      assertEquals(req.getValues().size(), 2);
      TestUtil.assertPriceEqualsQuote(req.getValues().get(0), quote3);
      TestUtil.assertPriceEqualsQuote(req.getValues().get(1), quote4);
      return 10;

    } else {
      fail("Unknown Symbol");
      return 2;
    }
  }
}
