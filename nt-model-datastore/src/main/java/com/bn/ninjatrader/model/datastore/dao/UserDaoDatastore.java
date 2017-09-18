package com.bn.ninjatrader.model.datastore.dao;

import com.bn.ninjatrader.common.model.User;
import com.bn.ninjatrader.common.util.IdGenerator;
import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.model.datastore.document.UserDocument;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Key;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class UserDaoDatastore implements UserDao {
  private static final Logger LOG = LoggerFactory.getLogger(UserDaoDatastore.class);

  private final Clock clock;
  private final IdGenerator idGenerator;

  @Inject
  public UserDaoDatastore(final Clock clock, final IdGenerator idGenerator) {
    this.clock = clock;
    this.idGenerator = idGenerator;
  }

  @Override
  public User saveUser(final User user) {
    final UserDocument doc = UserDocument.copyFrom(user);
    if (StringUtils.isEmpty(doc.getUserId())) {
      doc.setUserId(idGenerator.createId());
    }
    ofy().save().entities(doc).now();
    return doc.toUser();
  }

  @Override
  public Optional<User> findByUserId(final String userId) {
    final UserDocument doc = ofy().load().key(Key.create(UserDocument.class, userId)).now();
    return Optional.ofNullable(doc).map(d -> d.toUser());
  }

  @Override
  public Optional<User> findByUsername(final String username) {
    final List<UserDocument> docs = ofy().load().type(UserDocument.class).filter("username = ", username).list();
    return docs.stream().map(doc -> doc.toUser()).findFirst();
  }
}
