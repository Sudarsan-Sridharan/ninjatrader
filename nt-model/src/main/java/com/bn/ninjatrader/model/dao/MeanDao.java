package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.annotation.DailyMeanCollection;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class MeanDao extends AbstractValueDao {

  private static final Logger log = LoggerFactory.getLogger(MeanDao.class);

  @Inject
  public MeanDao(@DailyMeanCollection MongoCollection mongoCollection) {
    super(mongoCollection);
  }
}
