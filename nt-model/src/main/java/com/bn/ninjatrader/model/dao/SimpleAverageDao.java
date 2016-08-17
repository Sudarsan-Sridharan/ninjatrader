package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.annotation.DailySimpleAverageCollection;
import com.bn.ninjatrader.model.dao.period.AbstractPeriodDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class SimpleAverageDao extends AbstractPeriodDao {

  private static final Logger log = LoggerFactory.getLogger(SimpleAverageDao.class);

  @Inject
  public SimpleAverageDao(@DailySimpleAverageCollection MongoCollection mongoCollection) {
    super(mongoCollection);
  }
}
