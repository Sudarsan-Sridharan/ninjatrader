package com.bn.ninjatrader.model.datastore.dao.operation;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.datastore.document.PriceDocument;
import com.bn.ninjatrader.common.model.Price;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.googlecode.objectify.Key;

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
  private String symbol;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;
  private Collection<Price> prices = Collections.emptyList();

  public DatastoreSavePricesOperation(Collection<Price> prices) {
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
    // Organize prices by year
    final Multimap<Integer, Price> pricesPerYear = ArrayListMultimap.create();
    for (final Price price : prices) {
      pricesPerYear.put(price.getDate().getYear(), price);
    }

    // For each year, create createKey
    final List<Key<PriceDocument>> keys = Lists.newArrayList();
    for (int year : pricesPerYear.keySet()) {
      keys.add(createKey(symbol, year, timeFrame));
    }

    // Load documents where the prices belong
    final Map<Key<PriceDocument>, PriceDocument> documents = ofy().load().keys(keys);

    // Add prices to their respective documents
    for (int year : pricesPerYear.keySet()) {
      final Key<PriceDocument> key = createKey(symbol, year, timeFrame);
      // Create new document if not existing
      if (documents.get(key) == null) {
        documents.put(key, new PriceDocument(symbol, year, timeFrame));
      }

      // Add prices to document. Must not have duplicate dates. Existing date will be overwritten.
      final Collection<Price> pricesToSave = pricesPerYear.get(year);
      final Map<LocalDate, Price> docPrices = Maps.newHashMap();

      // Add document prices to a map, ensuring uniqueness by date.
      for (final Price price : documents.get(key).getData()) {
        docPrices.put(price.getDate(), price);
      }

      // Add new prices to map, overwriting old prices of same date.
      for (final Price price : pricesToSave) {
        docPrices.put(price.getDate(), price);
      }

      documents.get(key).setData(docPrices.values().stream().collect(Collectors.toList()));
    }

    // Save all documents
    ofy().save().entities(documents.values()).now();
  }

  private Key<PriceDocument> createKey(final String symbol, final int year, final TimeFrame timeFrame) {
    return Key.create(PriceDocument.class, PriceDocument.id(symbol, year, timeFrame));
  }
}
