package com.bn.ninjatrader.model.mongo.dao.query;

/**
 * @author bradwee2000@gmail.com
 */
public class MongoAlgorithmQueries {

  public static final String FIND_BY_ALGO_ID = "{ algoId: # }";
  public static final String FIND_BY_USER_ID = "{ userId: # }";
  public static final String FIND_BY_AUTO_SCAN = "{ isAutoScan: # }";
  public static final String FIND_BY_USER_AND_ALGO_ID = "{ userId: #, algoId: # }";
  public static final String FIND_BY_USER_ID_AND_AUTO_SCAN = "{ userId: #, isAutoScan: # }";

  private MongoAlgorithmQueries() {}
}
