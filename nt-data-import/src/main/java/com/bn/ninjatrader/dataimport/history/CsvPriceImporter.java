package com.bn.ninjatrader.dataimport.history;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.dataimport.history.parser.CsvDataParser;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.DailyQuote;
import com.bn.ninjatrader.model.entity.Price;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Brad on 4/28/16.
 */
@Singleton
public class CsvPriceImporter {

  private static final Logger LOG = LoggerFactory.getLogger(CsvPriceImporter.class);
  private static final String[] SUPPORTED_FORMATS = new String[]{"csv"};
  private static final String DEFAULT_DIR = "/Users/a-/Downloads/stock/";

  private final CsvDataParser parser;
  private final PriceDao priceDao;

  @Inject
  public CsvPriceImporter(final CsvDataParser parser,
                          final PriceDao priceDao) {
    this.parser = parser;
    this.priceDao = priceDao;
  }

  public void importPrices() throws IOException {
    importPricesFromDir(DEFAULT_DIR);
  }

  public void importPricesFromDir(final String directory) throws IOException {
    final File dir = new File(directory);
    final Collection<File> files = FileUtils.listFiles(dir, SUPPORTED_FORMATS, true);
    final Set<DailyQuote> quotes = Sets.newHashSet(); // Use Set to avoid duplicates

    for (final File file : files) {
      LOG.debug("Importing file: {}", file.getAbsoluteFile());
      final List<DailyQuote> parsedQuotes = parser.parse(file);
      quotes.addAll(parsedQuotes);
    }
    save(quotes);
  }

  /**
   * Convert List of prices to document and execute.
   *
   * @param quotes
   */
  public void save(final Collection<DailyQuote> quotes) {
    final Multimap<String, Price> symbolMultimap = ArrayListMultimap.create();

    for (final DailyQuote quote : quotes) {
      symbolMultimap.put(quote.getSymbol(), quote.getPrice());
    }

    for (final Map.Entry<String, Collection<Price>> perSymbol : symbolMultimap.asMap().entrySet()) {
      final String symbol = perSymbol.getKey();
      priceDao.savePrices(perSymbol.getValue()).withSymbol(symbol).withTimeFrame(TimeFrame.ONE_DAY).now();
    }
  }
}
