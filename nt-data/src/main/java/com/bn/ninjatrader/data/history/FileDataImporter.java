package com.bn.ninjatrader.data.history;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.data.history.parser.CsvDataParser;
import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Brad on 4/28/16.
 */
@Singleton
public class FileDataImporter {

  private static final Logger log = LoggerFactory.getLogger(FileDataImporter.class);
  private static final String[] SUPPORTED_FORMATS = new String[] {"csv"};
  private static final String DIR = "/Users/a-/Downloads/stock";

  @Inject
  private CsvDataParser parser;

  @Inject
  private PriceDao priceDao;

  public void importData() throws Exception {
    File dir = new File(DIR);
    Collection<File> files = FileUtils.listFiles(dir, SUPPORTED_FORMATS, true);
    List<DailyQuote> quotes = Lists.newArrayList();

    for (File file : files) {
      log.debug("Importing file: {}", file.getAbsoluteFile());
      quotes.addAll(parser.parse(file));
    }

    save(quotes);
  }

  /**
   * Convert List of prices to document and save.
   * @param quotes
   */
  public void save(List<DailyQuote> quotes) {
    Map<String, List<Price>> symbolMap = Maps.newLinkedHashMap();

    for (DailyQuote quote : quotes) {
      // Get or create price list for symbol
      List<Price> prices = symbolMap.get(quote.getSymbol());
      if (prices == null) {
        prices = Lists.newArrayList();
        symbolMap.put(quote.getSymbol(), prices);
      }

      prices.add(new Price(
          quote.getOpen(),
          quote.getHigh(),
          quote.getLow(),
          quote.getClose(),
          quote.getVolume(),
          quote.getDate()));
    }

    // Save price list for each symbol
    for (Map.Entry<String, List<Price>> perSymbol : symbolMap.entrySet()) {
      String symbol = perSymbol.getKey();
      priceDao.save(symbol, perSymbol.getValue());
    }
  }

  public static void main(String args[]) throws Exception {
    Injector injector = Guice.createInjector(
        new NtModelModule()
    );

    FileDataImporter app = injector.getInstance(FileDataImporter.class);
    app.importData();
  }
}
