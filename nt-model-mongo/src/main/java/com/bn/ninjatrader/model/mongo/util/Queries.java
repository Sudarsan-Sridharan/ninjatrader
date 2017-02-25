package com.bn.ninjatrader.model.mongo.util;

/**
 * Created by Brad on 7/26/16.
 */
public class Queries {

  private Queries() {}

  public static final String FIND_BY_SYMBOL = createQuery(QueryParam.SYMBOL);

  public static final String FIND_BY_SYMBOL_TIMEFRAME = createQuery(QueryParam.SYMBOL, QueryParam.TIMEFRAME);

  public static final String FIND_BY_PERIOD = createQuery(QueryParam.SYMBOL, QueryParam.TIMEFRAME, QueryParam.YEAR, QueryParam.PERIOD);

  public static final String FIND_BY_SYMBOL_TIMEFRAME_YEAR = createQuery(QueryParam.SYMBOL, QueryParam.TIMEFRAME, QueryParam.YEAR);

  public static final String FIND_BY_SYMBOL_TIMEFRAME_YEAR_RANGE = String.format("{%s:#, %s:#, %s:{$gte: #, $lte: #}}",
      QueryParam.SYMBOL, QueryParam.TIMEFRAME, QueryParam.YEAR);

  public static final String FIND_BY_PERIOD_YEAR_RANGE =
      String.format("{%s:#, %s:#, %s: {$gte: #, $lte: #}, %s:#}", QueryParam.SYMBOL, QueryParam.TIMEFRAME, QueryParam.YEAR, QueryParam.PERIOD);

  public static final String FIND_BY_YEAR = createQuery(QueryParam.YEAR);

  public static final String FIND_BY_TIMEFRAME_YEAR = createQuery(QueryParam.TIMEFRAME, QueryParam.YEAR);

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
