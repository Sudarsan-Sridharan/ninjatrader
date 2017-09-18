package com.bn.ninjatrader.model.datastore.dao;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.FixedList;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.datastore.dao.operation.DatastoreFindPricesOperation;
import com.bn.ninjatrader.model.datastore.dao.operation.DatastoreSavePricesOperation;
import com.bn.ninjatrader.model.datastore.document.PriceDocument;
import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceDaoDatastore implements PriceDao {
  private static final Logger LOG = LoggerFactory.getLogger(PriceDaoDatastore.class);
  public static final LocalDate MINIMUM_FROM_DATE = LocalDate.of(1999, 1, 1);

  private final Clock clock;

  @Inject
  public PriceDaoDatastore(final Clock clock) {
    this.clock = clock;
  }

  public Map<Key<PriceDocument>, PriceDocument> save(final PriceDocument document) {
    return ofy().save().entities(document).now();
  }

  @Override
  public SavePricesOperation savePrices(final Collection<Price> prices) {
    return new DatastoreSavePricesOperation(prices);
  }

  @Override
  public SavePricesOperation savePrices(Price price, Price... more) {
    return savePrices(Lists.asList(price, more));
  }

  @Override
  public DatastoreFindPricesOperation findPrices() {
    return new DatastoreFindPricesOperation();
  }

  @Override
  public Set<String> findAllSymbols() {
    final int thisYear = LocalDate.now(clock).getYear();
    final Set<String> symbols = Sets.newHashSet();

    final List<PriceDocument> documents = ofy().load().type(PriceDocument.class).filter("year = ", thisYear).list();
    for (final PriceDocument document : documents) {
      symbols.add(document.getSymbol());
    }
    return symbols;
  }

  @Override
  public List<Price> findBeforeDate(FindBeforeDateRequest request) {
    final FixedList<Price> bars = FixedList.withMaxSize(request.getNumOfValues());
    LocalDate fromDate = request.getBeforeDate().withDayOfYear(1);
    LocalDate toDate = request.getBeforeDate().minusDays(1);

    do {
      final List<Price> resultsPerYear = findPrices().withSymbol(request.getSymbol())
          .withTimeFrame(request.getTimeFrame())
          .from(fromDate)
          .to(toDate)
          .now();
      bars.clear();
      bars.addAll(resultsPerYear);
      fromDate = fromDate.minusYears(1);

      // If not enough data, reset the list and search again with a wider date range.
    } while (bars.size() < request.getNumOfValues() && fromDate.isAfter(MINIMUM_FROM_DATE));

    return bars.asList();

  }

  @Override
  public RenameSymbolOperation renameSymbol(String symbol) {
    // TODO
    return null;
  }

  private Key<PriceDocument> createKey(final String symbol, final int year, final TimeFrame timeFrame) {
    return Key.create(PriceDocument.class, PriceDocument.id(symbol, year, timeFrame));
  }

}
