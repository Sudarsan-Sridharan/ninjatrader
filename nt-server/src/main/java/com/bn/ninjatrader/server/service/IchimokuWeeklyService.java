package com.bn.ninjatrader.server.service;

import com.bn.ninjatrader.model.dao.WeeklyMeanDao;
import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * Created by Brad on 5/3/16.
 */
@Singleton
public class IchimokuWeeklyService extends IchimokuService {

  private static final Logger log = LoggerFactory.getLogger(IchimokuWeeklyService.class);

  @Inject
  public IchimokuWeeklyService(WeeklyPriceDao priceDao, WeeklyMeanDao meanDao) {
    super(priceDao, meanDao);
  }

  @Override
  protected int getNumOfPastMonths() {
    return 6; // need enough months of past data to fully calculate present data
  }
}
