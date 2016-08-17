package com.bn.ninjatrader.model.util;

import static com.bn.ninjatrader.model.util.QueryParamName.*;

/**
 * Created by Brad on 7/26/16.
 */
public class Queries {

  private Queries() {}

  public static final String FIND_BY_SYMBOL = String.format("{%s : #}", SYMBOL);

  public static final String FIND_BY_PERIOD = String.format("{%s : #, %s: #, %s: #}", SYMBOL, YEAR, PERIOD);

  public static final String FIND_BY_YEAR = String.format("{%s : #, %s: #}", SYMBOL, YEAR);

  public static final String FIND_BY_YEAR_RANGE = String.format("{%s : #, %s: {$gte: #, $lte: #}}", SYMBOL, YEAR);

  public static final String FIND_BY_PERIOD_YEAR_RANGE =
      String.format("{%s : #, %s: {$gte: #, $lte: #}, %s : #}", SYMBOL, YEAR, PERIOD);

  public static final String FIND_ALL_FOR_YEAR = String.format("{%s : #}", YEAR);

  public static final String FIND_SETTING_BY_NAME = String.format("{%s : #}", SETTING_NAME);
}
