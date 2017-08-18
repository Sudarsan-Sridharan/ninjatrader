package com.bn.ninjatrader.model.mongo.dao.operation;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.model.mongo.document.MongoAlgorithmDocument;
import com.google.common.collect.Lists;
import org.jongo.Find;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.bn.ninjatrader.model.mongo.dao.query.MongoAlgorithmQueries.FIND_BY_AUTO_SCAN;
import static com.bn.ninjatrader.model.mongo.dao.query.MongoAlgorithmQueries.FIND_BY_USER_ID;
import static com.bn.ninjatrader.model.mongo.dao.query.MongoAlgorithmQueries.FIND_BY_USER_ID_AND_AUTO_SCAN;

/**
 * @author bradwee2000@gmail.com
 */
public class MongoFindAlgorithmsOperation implements AlgorithmDao.FindAlgorithmsOperation {
  private static final Logger LOG = LoggerFactory.getLogger(MongoFindAlgorithmsOperation.class);

  private final MongoCollection mongoCollection;

  private String userId;
  private Boolean isAutoScan;

  public MongoFindAlgorithmsOperation(final MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;
  }

  @Override
  public MongoFindAlgorithmsOperation withUserId(final String userId) {
    this.userId = userId;
    return this;
  }

  @Override
  public MongoFindAlgorithmsOperation isAutoScan(final boolean isAutoScan) {
    this.isAutoScan = isAutoScan;
    return this;
  }

  @Override
  public List<Algorithm> now() {
    try (final MongoCursor<MongoAlgorithmDocument> cursor = prepareQuery()) {
      return Lists.newArrayList(cursor.iterator()).stream()
          .map(algo -> algo.toTradeAlgorithm())
          .sorted(Comparator.comparing(Algorithm::getDescription))
          .collect(Collectors.toList());
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private MongoCursor<MongoAlgorithmDocument> prepareQuery() {
    final Find find;
    if (userId != null && isAutoScan != null) {
      find = mongoCollection.find(FIND_BY_USER_ID_AND_AUTO_SCAN, userId, true);
    } else if (userId != null) {
      find = mongoCollection.find(FIND_BY_USER_ID, userId);
    } else {
      find = mongoCollection.find(FIND_BY_AUTO_SCAN, isAutoScan);
    }
    return find.as(MongoAlgorithmDocument.class);
  }
}
