package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.annotation.EMACollection;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class EMADao extends AbstractValueDao {

  private static final Logger LOG = LoggerFactory.getLogger(EMADao.class);

  @Inject
  public EMADao(@EMACollection final MongoCollection mongoCollection) {
    super(mongoCollection);
  }
}
