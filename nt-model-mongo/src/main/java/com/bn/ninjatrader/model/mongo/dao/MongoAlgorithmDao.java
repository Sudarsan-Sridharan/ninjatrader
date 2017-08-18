package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.common.util.IdGenerator;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.model.mongo.annotation.TradeAlgorithmCollection;
import com.bn.ninjatrader.model.mongo.dao.operation.MongoFindAlgorithmsOperation;
import com.bn.ninjatrader.model.mongo.document.MongoAlgorithmDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.bn.ninjatrader.model.mongo.dao.query.MongoAlgorithmQueries.FIND_BY_ALGO_ID;
import static com.bn.ninjatrader.model.mongo.dao.query.MongoAlgorithmQueries.FIND_BY_USER_AND_ALGO_ID;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class MongoAlgorithmDao extends MongoAbstractDao implements AlgorithmDao {
  private static final Logger LOG = LoggerFactory.getLogger(MongoAlgorithmDao.class);

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
    final MongoAlgorithmDocument doc = MongoAlgorithmDocument.copyFrom(algo);
    if (StringUtils.isEmpty(doc.getAlgorithmId())) {
      doc.setAlgorithmId(idGenerator.createId());
    }

    getMongoCollection().update(FIND_BY_ALGO_ID, doc.getAlgorithmId()).upsert().with(doc);
    return doc.toTradeAlgorithm();
  }

  @Override
  public Optional<Algorithm> findOneByAlgorithmId(final String algoId) {
    final Optional<MongoAlgorithmDocument> foundDoc = Optional.ofNullable(getMongoCollection()
        .findOne(FIND_BY_ALGO_ID, algoId).as(MongoAlgorithmDocument.class));

    return foundDoc.map(doc -> doc.toTradeAlgorithm());
  }

  @Override
  public void delete(final String algoId) {
    getMongoCollection().remove(FIND_BY_ALGO_ID, algoId);
  }

  @Override
  public void delete(final String userId, final String algoId) {
    getMongoCollection().remove(FIND_BY_USER_AND_ALGO_ID, userId, algoId);
  }

  @Override
  public FindAlgorithmsOperation findAlgorithms() {
    return new MongoFindAlgorithmsOperation(getMongoCollection());
  }
}
