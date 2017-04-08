package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.mongo.annotation.TradeAlgorithmCollection;
import com.bn.ninjatrader.model.mongo.document.MongoTradeAlgorithmDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class MongoTradeAlgorithmDao extends MongoAbstractDao implements TradeAlgorithmDao {
  private static final Logger LOG = LoggerFactory.getLogger(MongoTradeAlgorithmDao.class);
  private static final String FIND_BY_ALGO_ID = "{ algoId: # }";
  private static final String FIND_BY_USER_ID = "{ userId: # }";

  @Inject
  public MongoTradeAlgorithmDao(@TradeAlgorithmCollection final MongoCollection mongoCollection) {
    super(mongoCollection);
    mongoCollection.ensureIndex(Queries.createIndex("algoId"), "{unique: true}");
  }

  @Override
  public void save(final TradeAlgorithm algo) {
    final MongoTradeAlgorithmDocument doc = MongoTradeAlgorithmDocument.copyFrom(algo);
    getMongoCollection().update(FIND_BY_ALGO_ID, doc.getTradeAlgorithmId()).upsert().with(doc);
  }

  @Override
  public Optional<TradeAlgorithm> findByTradeAlgorithmId(final String algoId) {
    final Optional<MongoTradeAlgorithmDocument> foundDoc = Optional.ofNullable(getMongoCollection()
        .findOne(FIND_BY_ALGO_ID, algoId).as(MongoTradeAlgorithmDocument.class));

    return foundDoc.map(doc -> doc.toTradeAlgorithm());
  }

  @Override
  public List<TradeAlgorithm> findByUserId(final String userId) {
    try (final MongoCursor<MongoTradeAlgorithmDocument> cursor =
             getMongoCollection().find(FIND_BY_USER_ID, userId)
                 .as(MongoTradeAlgorithmDocument.class)) {

      return Lists.newArrayList(cursor.iterator()).stream()
          .map(algo -> algo.toTradeAlgorithm())
          .sorted((algo1, algo2) -> algo1.getDescription().compareTo(algo2.getDescription()))
          .collect(Collectors.toList());

    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
