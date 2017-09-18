package com.bn.ninjatrader.model.datastore.dao;

import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.common.util.IdGenerator;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.datastore.dao.operation.DatastoreFindAlgorithmsOperation;
import com.bn.ninjatrader.model.datastore.document.AlgorithmDocument;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Key;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.Optional;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AlgorithmDaoDatastore implements AlgorithmDao {
  private static final Logger LOG = LoggerFactory.getLogger(AlgorithmDaoDatastore.class);

  private final Clock clock;
  private final IdGenerator idGenerator;

  @Inject
  public AlgorithmDaoDatastore(final Clock clock, final IdGenerator idGenerator) {
    this.clock = clock;
    this.idGenerator = idGenerator;
  }

  @Override
  public Algorithm save(Algorithm algorithm) {
    final AlgorithmDocument doc = AlgorithmDocument.copyFrom(algorithm);
    if (StringUtils.isEmpty(doc.getId())) {
      doc.setId(idGenerator.createId());
    }
    ofy().save().entities(doc).now();
    return doc.toAlgorithm();
  }

  @Override
  public FindAlgorithmsOperation findAlgorithms() {
    return new DatastoreFindAlgorithmsOperation();
  }

  @Override
  public Optional<Algorithm> findOneByAlgorithmId(String algorithmId) {
    final AlgorithmDocument doc = ofy().load().key(Key.create(AlgorithmDocument.class, algorithmId)).now();
    return Optional.ofNullable(doc).map(d -> d.toAlgorithm());
  }

  @Override
  public void delete(String algorithmId) {
    ofy().delete().key(Key.create(AlgorithmDocument.class, algorithmId));
  }

  @Override
  public void delete(String userId, String algorithmId) {
    ofy().delete().key(Key.create(AlgorithmDocument.class, algorithmId)); // TODO check that algo belongs to user!
  }
}
