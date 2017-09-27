package com.bn.ninjatrader.model.datastore.dao.operation;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.datastore.document.PriceDocument;
import com.googlecode.objectify.Key;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Builder for finding prices operation
 */
public final class DatastoreFindPricesOperation implements PriceDao.FindPricesOperation {
  private final Map<String, List<Price>> priceCache;
  private String symbol;
  private LocalDate from;
  private LocalDate to;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;

  public DatastoreFindPricesOperation(final Map<String, List<Price>> priceCache) {
    this.priceCache = priceCache;
  }

  @Override
  public DatastoreFindPricesOperation withSymbol(final String symbol) {
    this.symbol = symbol;
    return this;
  }

  @Override
  public DatastoreFindPricesOperation from(final LocalDate from) {
    this.from = from;
    return this;
  }

  @Override
  public DatastoreFindPricesOperation to(final LocalDate to) {
    this.to = to;
    return this;
  }

  @Override
  public DatastoreFindPricesOperation withTimeFrame(final TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  @Override
  public List<Price> now() {
    final List<PriceDocument> cachedDocs = IntStream.range(from.getYear(), to.getYear() + 1)
        .mapToObj(year -> new PriceDocument(symbol, year, timeFrame)
            .setData(priceCache.get(PriceDocument.id(symbol, year, timeFrame))))
        .filter(doc -> doc.getData() != null && !doc.getData().isEmpty())
        .collect(Collectors.toList());

    // Collect years not found in cache
    final List<Integer> availableYears = cachedDocs.stream().map(doc -> doc.getYear()).collect(Collectors.toList());
    final List<Key<PriceDocument>> missingKeys = IntStream.range(from.getYear(), to.getYear() + 1)
        .filter(year -> !availableYears.contains(year))
        .mapToObj(year -> Key.create(PriceDocument.class, PriceDocument.id(symbol, year, timeFrame)))
        .collect(Collectors.toList());

    // Find missing documents from database
    final Map<Key<PriceDocument>, PriceDocument> dbDocs = ofy().load().keys(missingKeys);

    // Store missing docs to cache
    dbDocs.values().stream().forEach(doc -> priceCache.put(doc.getKey(), doc.getData()));

    // Merge all docs
    cachedDocs.addAll(dbDocs.values());

    // Collect prices from all docs
    return cachedDocs.stream()
        .sorted(Comparator.comparing(PriceDocument::getYear))
        .flatMap(doc -> doc.getData().stream())
        .filter(d -> !d.getDate().isBefore(from) && !d.getDate().isAfter(to))
        .collect(Collectors.toList());
  }
}
