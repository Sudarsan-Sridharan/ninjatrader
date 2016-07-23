package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.annotation.WeeklyMeanCollection;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class WeeklyMeanDao extends MeanDao {

  @Inject
  public WeeklyMeanDao(@WeeklyMeanCollection MongoCollection mongoCollection) {
    super(mongoCollection);
  }
}
