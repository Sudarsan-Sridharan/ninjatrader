package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.RSIValue;
import com.bn.ninjatrader.model.annotation.RSICollection;
import com.bn.ninjatrader.model.document.RSIDocument;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DAO for Relative Strength Index values.
 *
 * Created by Brad on 4/30/16.
 */
@Singleton
public class RSIDao extends AbstractValueDao<RSIValue> {

  private static final Logger LOG = LoggerFactory.getLogger(RSIDao.class);

  @Inject
  public RSIDao(@RSICollection final MongoCollection mongoCollection) {
    super(mongoCollection);
  }

  @Override
  public Class getDocumentClass() {
    return RSIDocument.class;
  }

  @Override
  public List<RSIValue> findBeforeDate(final FindBeforeDateRequest request) {
    return super.findBeforeDate(request);
  }
}
