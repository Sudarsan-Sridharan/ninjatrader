package com.bn.ninjatrader.model.appengine.dao;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.model.appengine.request.FindPriceRequest;
import com.bn.ninjatrader.model.appengine.PriceDocument;
import com.bn.ninjatrader.model.appengine.request.SavePriceRequest;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.googlecode.objectify.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.appengine.repackaged.com.google.api.client.util.Preconditions.checkNotNull;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceDaoGae {
  private static final Logger LOG = LoggerFactory.getLogger(PriceDaoGae.class);

  public Map<Key<PriceDocument>, PriceDocument> save(final PriceDocument document) {
    return ofy().save().entities(document).now();
  }

  public Map<Key<PriceDocument>, PriceDocument> save(final SavePriceRequest request) {
    // Organize prices by year
    final Multimap<Integer, Price> pricesPerYear = ArrayListMultimap.create();
    for (final Price price : request.getPrices()) {
      pricesPerYear.put(price.getDate().getYear(), price);
    }

    // For each year, create createKey
    final List<Key<PriceDocument>> keys = Lists.newArrayList();
    for (int year : pricesPerYear.keySet()) {
      keys.add(createKey(request.getSymbol(), year, request.getTimeFrame()));
    }

    // Load documents where the prices belong
    final Map<Key<PriceDocument>, PriceDocument> documents = ofy().load().keys(keys);

    // Add prices to their respective documents
    for (int year : pricesPerYear.keySet()) {
      final Key<PriceDocument> key = createKey(request.getSymbol(), year, request.getTimeFrame());
      // Create new document if not existing
      if (documents.get(key) == null) {
        documents.put(key, new PriceDocument(request.getSymbol(), year, request.getTimeFrame()));
      }

      // Add prices to document. Must not have duplicate dates. Existing date will be overwritten.
      final Collection<Price> pricesToSave = pricesPerYear.get(year);
      final Map<LocalDate, Price> docPrices = documents.get(key).getData().stream()
          .collect(Collectors.toMap(price -> price.getDate(), price -> price));
      docPrices.putAll(pricesToSave.stream().collect(Collectors.toMap(price -> price.getDate(), price -> price)));
      documents.get(key).setData(docPrices.values().stream().collect(Collectors.toList()));
    }

    // Save all documents
    return ofy().save().entities(documents.values()).now();
  }

  public List<Price> find(final FindPriceRequest request) {
    checkNotNull(request, "FindPriceRequest must not be null.");
    checkNotNull(request.getSymbol(), "FindPriceRequest.symbol must not be null.");
    checkNotNull(request.getFromDate(), "FindPriceRequest.fromDate must not be null.");
    checkNotNull(request.getToDate(), "FindPriceRequest.toDate must not be null.");

    final int fromYear = request.getFromDate().getYear();
    final int toYear = request.getToDate().getYear();

    // Create keys for each year
    final List<Key<PriceDocument>> keys = Lists.newArrayList();
    for (int i = fromYear; i <= toYear; i++) {
      keys.add(Key.create(PriceDocument.class, PriceDocument.id(request.getSymbol(), i, request.getTimeFrame())));
    }

    // Find documents
    final Map<Key<PriceDocument>, PriceDocument> results = ofy().load().keys(keys);

    // Collect prices from each document
    final List<Price> prices = Lists.newArrayList();
    for (final PriceDocument document : results.values()) {
      prices.addAll(document.getData());
    }

    Collections.sort(prices);

    DateObjUtil.trimToDateRange(prices, request.getFromDate(), request.getToDate());

    return prices;
  }

  private Key<PriceDocument> createKey(final String symbol, final int year, final TimeFrame timeFrame) {
    return Key.create(PriceDocument.class, PriceDocument.id(symbol, year, timeFrame));
  }
}
