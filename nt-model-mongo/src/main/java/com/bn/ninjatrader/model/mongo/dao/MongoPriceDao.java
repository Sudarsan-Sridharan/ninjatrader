package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.FixedList;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.model.mongo.annotation.PriceCollection;
import com.bn.ninjatrader.model.mongo.dao.operation.MongoFindPricesOperation;
import com.bn.ninjatrader.model.mongo.dao.operation.MongoRenameSymbolOperation;
import com.bn.ninjatrader.model.mongo.dao.operation.MongoSavePricesOperation;
import com.bn.ninjatrader.model.mongo.document.MongoPriceDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.bn.ninjatrader.model.mongo.util.QueryParam.*;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class MongoPriceDao extends MongoAbstractDao implements PriceDao {
  private static final Logger LOG = LoggerFactory.getLogger(MongoPriceDao.class);

  private final Clock clock;

  @Inject
  public MongoPriceDao(@PriceCollection final MongoCollection priceCollection,
                       final Clock clock) {
    super(priceCollection);
    this.clock = clock;

    priceCollection.ensureIndex(Queries.createIndex(SYMBOL, TIMEFRAME, YEAR),
        "{unique: true}");
  }

  public MongoFindPricesOperation findPrices() {
    return new MongoFindPricesOperation(getMongoCollection());
  }

  @Override
  public MongoSavePricesOperation savePrices(final Collection<Price> prices) {
    return new MongoSavePricesOperation(getMongoCollection(), prices);
  }

  @Override
  public MongoSavePricesOperation savePrices(final Price price, final Price ... more) {
    return savePrices(Lists.asList(price, more));
  }

  @Override
  public Set<String> findAllSymbols() {
    final int thisYear = LocalDate.now(clock).getYear();
    final Set<String> symbols = Sets.newHashSet();

    try (final MongoCursor<MongoPriceDocument> cursor = getMongoCollection()
            .find(Queries.FIND_BY_TIMEFRAME_YEAR, TimeFrame.ONE_DAY, thisYear)
            .as(MongoPriceDocument.class)) {
      while (cursor.hasNext()) {
        symbols.add(cursor.next().getSymbol());
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return symbols;
  }

  @Override
  public List<Price> findBeforeDate(final FindBeforeDateRequest request) {
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
    return new MongoRenameSymbolOperation(this, symbol);
  }
}
