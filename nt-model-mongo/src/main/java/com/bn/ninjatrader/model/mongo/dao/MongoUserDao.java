package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.common.model.User;
import com.bn.ninjatrader.model.mongo.annotation.UserCollection;
import com.bn.ninjatrader.model.mongo.document.MongoUserDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class MongoUserDao extends MongoAbstractDao implements UserDao {
  private static final Logger LOG = LoggerFactory.getLogger(MongoUserDao.class);

  @Inject
  public MongoUserDao(@UserCollection final MongoCollection mongoCollection) {
    super(mongoCollection);
    mongoCollection.ensureIndex(Queries.createIndex("userId"), "{unique: true}");
    mongoCollection.ensureIndex(Queries.createIndex("email"), "{unique: true}");
    mongoCollection.ensureIndex(Queries.createIndex("username"), "{unique: true}");
  }

  @Override
  public void saveUser(final User user) {
    final MongoUserDocument document = MongoUserDocument.copyFrom(user);
    getMongoCollection().update("{userId:#}", document.getUserId()).upsert().with(document);
  }

  @Override
  public Optional<User> findByUserId(final String userId) {
    final Optional<MongoUserDocument> foundDocument = Optional.ofNullable(getMongoCollection()
        .findOne("{userId:#}", userId)
        .as(MongoUserDocument.class));
    return foundDocument.map(doc -> doc.toUser());
  }

  @Override
  public Optional<User> findByUsername(String username) {
    final Optional<MongoUserDocument> foundDocument = Optional.ofNullable(getMongoCollection()
        .findOne("{username:#}", username)
        .as(MongoUserDocument.class));
    return foundDocument.map(doc -> doc.toUser());
  }
}
