package com.bn.ninjatrader.model.datastore.dao.operation;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.datastore.document.PriceDocument;
import com.googlecode.objectify.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Builder for finding prices operation
 */
public final class DatastoreSavePricesOperation implements PriceDao.SavePricesOperation {
  private static final Logger LOG = LoggerFactory.getLogger(DatastoreSavePricesOperation.class);

  private String symbol;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;
  private Map<String, List<Price>> priceCache;
  private Collection<Price> prices = Collections.emptyList();

  public DatastoreSavePricesOperation(final Map<String, List<Price>> priceCache,
                                      final Collection<Price> prices) {
    this.priceCache = priceCache;
    this.prices = prices;
  }

  @Override
  public DatastoreSavePricesOperation withSymbol(final String symbol) {
    this.symbol = symbol;
    return this;
  }

  @Override
  public DatastoreSavePricesOperation withTimeFrame(final TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  @Override
  public void now() {
    // Group prices by year
    final Map<Integer, List<Price>> pricesByYear = prices.stream()
        .collect(Collectors.groupingBy(price -> price.getDate().getYear(), Collectors.toList()));

    // For each year, create key for faster access in datastore
    final List<Key<PriceDocument>> keys = pricesByYear.keySet().stream()
        .map(year -> createKey(symbol, year, timeFrame))
        .collect(Collectors.toList());

    // Load documents where the prices belong
    final Map<Key<PriceDocument>, PriceDocument> documents = ofy().load().keys(keys);

    // Add prices to their respective documents
    for (int year : pricesByYear.keySet()) {

      // Create new document if not existing
      final Key<PriceDocument> key = createKey(symbol, year, timeFrame);
      if (documents.get(key) == null) {
        documents.put(key, new PriceDocument(symbol, year, timeFrame));
      }

      // Add document prices to a map, ensuring uniqueness by date.
      final Map<LocalDate, Price> docPrices = documents.get(key).getData().stream()
          .collect(Collectors.toMap((price) -> price.getDate(), (price) -> price));

      // Add new prices to map, overwriting old prices of same date.
      pricesByYear.get(year).stream()
          .forEach(price -> docPrices.put(price.getDate(), price));

      documents.get(key).setData(docPrices.values().stream().collect(Collectors.toList()));
    }

    // Save all documents
    final Map<Key<PriceDocument>, PriceDocument> savedDocs = ofy().save().entities(documents.values()).now();

    // Update cache
    updateCache(savedDocs);
  }

  /**
   * Create Datastore Key
   */
  private Key<PriceDocument> createKey(final String symbol, final int year, final TimeFrame timeFrame) {
    return Key.create(PriceDocument.class, PriceDocument.id(symbol, year, timeFrame));
  }

  /**
   * Update the cache, if it exists, with new prices.
   */
  private void updateCache(Map<Key<PriceDocument>, PriceDocument> savedDocs) {
    if (priceCache != null) {
      final Map<String, List<Price>> savedPrices = savedDocs.keySet().stream()
          .collect(Collectors.toMap(key -> key.getName(), key -> savedDocs.get(key).getData()));
      priceCache.putAll(savedPrices);
    }
  }
}
