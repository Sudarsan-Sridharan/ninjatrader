package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.annotation.DailyRSICollection;
import com.bn.ninjatrader.model.dao.period.AbstractPeriodDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO for Relative Strength Index values.
 *
 * Created by Brad on 4/30/16.
 */
@Singleton
public class RSIDao extends AbstractPeriodDao {

  private static final Logger log = LoggerFactory.getLogger(RSIDao.class);

  @Inject
  public RSIDao(@DailyRSICollection MongoCollection mongoCollection) {
    super(mongoCollection);
  }
}
