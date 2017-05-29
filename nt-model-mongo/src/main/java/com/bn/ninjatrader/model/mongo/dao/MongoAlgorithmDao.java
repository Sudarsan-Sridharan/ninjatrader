package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.entity.Algorithm;
import com.bn.ninjatrader.model.mongo.annotation.TradeAlgorithmCollection;
import com.bn.ninjatrader.model.mongo.document.MongoTradeAlgorithmDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.bn.ninjatrader.common.util.IdGenerator;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
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
public class MongoAlgorithmDao extends MongoAbstractDao implements AlgorithmDao {
  private static final Logger LOG = LoggerFactory.getLogger(MongoAlgorithmDao.class);
  private static final String FIND_BY_ALGO_ID = "{ algoId: # }";
  private static final String FIND_BY_USER_ID = "{ userId: # }";
  private static final String FIND_BY_USER_ALGO_ID = "{ userId: #, algoId: # }";

  private final IdGenerator idGenerator;

  @Inject
  public MongoAlgorithmDao(@TradeAlgorithmCollection final MongoCollection mongoCollection,
                           final IdGenerator idGenerator) {
    super(mongoCollection);
    this.idGenerator = idGenerator;
    mongoCollection.ensureIndex(Queries.createIndex("algoId"), "{unique: true}");
  }

  @Override
  public Algorithm save(final Algorithm algo) {
    final MongoTradeAlgorithmDocument doc = MongoTradeAlgorithmDocument.copyFrom(algo);
    if (StringUtils.isEmpty(doc.getAlgorithmId())) {
      doc.setAlgorithmId(idGenerator.createId());
    }
    getMongoCollection().update(FIND_BY_ALGO_ID, doc.getAlgorithmId()).upsert().with(doc);
    return doc.toTradeAlgorithm();
  }

  @Override
  public Optional<Algorithm> findByAlgorithmId(final String algoId) {
    final Optional<MongoTradeAlgorithmDocument> foundDoc = Optional.ofNullable(getMongoCollection()
        .findOne(FIND_BY_ALGO_ID, algoId).as(MongoTradeAlgorithmDocument.class));

    return foundDoc.map(doc -> doc.toTradeAlgorithm());
  }

  @Override
  public void delete(final String algoId) {
    getMongoCollection().remove(FIND_BY_ALGO_ID, algoId);
  }

  @Override
  public void delete(final String userId, final String algoId) {
    getMongoCollection().remove(FIND_BY_USER_ALGO_ID, userId, algoId);
  }

  @Override
  public List<Algorithm> findByUserId(final String userId) {
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
