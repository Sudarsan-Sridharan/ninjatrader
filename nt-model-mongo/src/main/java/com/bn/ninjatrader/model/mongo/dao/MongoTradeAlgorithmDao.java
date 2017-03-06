package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.mongo.annotation.TradeAlgorithmCollection;
import com.bn.ninjatrader.model.mongo.document.MongoTradeAlgorithmDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.bn.ninjatrader.model.request.FindTradeAlgorithmRequest;
import com.bn.ninjatrader.model.request.SaveTradeAlgorithmRequest;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.jongo.Find;
import org.jongo.FindOne;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class MongoTradeAlgorithmDao extends AbstractDao implements TradeAlgorithmDao {

  @Inject
  public MongoTradeAlgorithmDao(@TradeAlgorithmCollection final MongoCollection mongoCollection) {
    super(mongoCollection);
    mongoCollection.ensureIndex(Queries.createIndex("algoId"), "{unique: true}");
  }

  @Override
  public List<TradeAlgorithm> save(final SaveTradeAlgorithmRequest request) {
    request.getTradeAlgorithms().stream()
        .map(algo -> MongoTradeAlgorithmDocument.copyFrom(algo))
        .forEach(algo -> getMongoCollection()
            .update("{ algoId: # }", algo.getTradeAlgorithmId())
            .upsert()
            .with(algo)
        );
    return request.getTradeAlgorithms();
  }

  @Override
  public List<TradeAlgorithm> find(final FindTradeAlgorithmRequest request) {
    // Create query based on parameters
    final Find find;
    if (!StringUtils.isEmpty(request.getTradeAlgorithmId())) {
      find = getMongoCollection().find("{ algoId: # }", request.getTradeAlgorithmId());
    } else {
      find = getMongoCollection().find("{ userId: # }", request.getUserId());
    }

    // Convert documents to TradeAlgorithm objects
    try (final MongoCursor<MongoTradeAlgorithmDocument> cursor = find.as(MongoTradeAlgorithmDocument.class)) {
      return Lists.newArrayList(cursor.iterator()).stream()
          .map(algo -> algo.toTradeAlgorithm())
          .collect(Collectors.toList());
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<TradeAlgorithm> findOne(final FindTradeAlgorithmRequest request) {
    // Create query based on parameters
    final FindOne find;
    if (!StringUtils.isEmpty(request.getTradeAlgorithmId())) {
      find = getMongoCollection().findOne("{ algoId: # }", request.getTradeAlgorithmId());
    } else {
      find = getMongoCollection().findOne("{ userId: # }", request.getUserId());
    }

    // Convert document to TradeAlgorithm object
    final MongoTradeAlgorithmDocument doc = find.as(MongoTradeAlgorithmDocument.class);
    return doc == null ? Optional.empty() : Optional.ofNullable(doc.toTradeAlgorithm());
  }
}
