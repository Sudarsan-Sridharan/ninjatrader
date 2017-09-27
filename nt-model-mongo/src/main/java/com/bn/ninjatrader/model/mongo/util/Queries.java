package com.bn.ninjatrader.model.mongo.util;

import static com.bn.ninjatrader.model.mongo.util.QueryParam.SYMBOL;
import static com.bn.ninjatrader.model.mongo.util.QueryParam.TIMEFRAME;
import static com.bn.ninjatrader.model.mongo.util.QueryParam.YEAR;

/**
 * Created by Brad on 7/26/16.
 */
public class Queries {

  private Queries() {}

  public static final String FIND_BY_SYMBOL = createQuery(SYMBOL);

  public static final String FIND_BY_SYMBOL_TIMEFRAME = createQuery(SYMBOL, TIMEFRAME);

  public static final String FIND_BY_PERIOD = createQuery(SYMBOL, TIMEFRAME, YEAR, QueryParam.PERIOD);

  public static final String FIND_BY_SYMBOL_TIMEFRAME_YEAR = createQuery(SYMBOL, TIMEFRAME, YEAR);

  public static final String FIND_BY_SYMBOL_TIMEFRAME_YEARS = String.format("{%s:#, %s:#, %s:{$in: #}}",
      SYMBOL, TIMEFRAME, YEAR);

  public static final String FIND_BY_SYMBOL_TIMEFRAME_YEAR_RANGE = String.format("{%s:#, %s:#, %s:{$gte: #, $lte: #}}",
      SYMBOL, TIMEFRAME, YEAR);

  public static final String FIND_BY_PERIOD_YEAR_RANGE =
      String.format("{%s:#, %s:#, %s: {$gte: #, $lte: #}, %s:#}", SYMBOL, TIMEFRAME, YEAR, QueryParam.PERIOD);

  public static final String FIND_BY_YEAR = createQuery(YEAR);

  public static final String FIND_BY_TIMEFRAME_YEAR = createQuery(TIMEFRAME, YEAR);

  public static final String FIND_BY_USER = createQuery(QueryParam.USER);

  public static final String FIND_BY_REPORT_ID = String.format("{%s.%s:#}", QueryParam.DATA, QueryParam.REPORT_ID);

  public static String createQuery(String param, String ... moreParams) {
    return createQueryWithArg("#", param, moreParams);
  }

  public static String createIndex(String param, String ... moreParams) {
    return createQueryWithArg("1", param, moreParams);
  }

  private static String createQueryWithArg(String arg, String param, String ... moreParams) {
    StringBuilder sb = new StringBuilder("{");
    sb.append(param).append(":").append(arg);
    for (String moreParam : moreParams) {
      sb.append(", ").append(moreParam).append(":").append(arg);
    }
    sb.append("}");
    return sb.toString();
  }
}
