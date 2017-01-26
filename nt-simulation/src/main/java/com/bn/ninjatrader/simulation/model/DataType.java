package com.bn.ninjatrader.simulation.model;

/**
 * Created by Brad on 8/2/16.
 */
public class DataType {

  private DataType() {}

  public static final String BAR_INDEX = "BAR_INDEX";

  public static final String PRICE_OPEN = "PRICE_OPEN";
  public static final String PRICE_HIGH = "PRICE_HIGH";
  public static final String PRICE_LOW = "PRICE_LOW";
  public static final String PRICE_CLOSE = "PRICE_CLOSE";
  public static final String VOLUME = "VOLUME";

  public static final String TENKAN = "TENKAN";
  public static final String KIJUN = "KIJUN";
  public static final String SENKOU_A = "SENKOU_A";
  public static final String SENKOU_B = "SENKOU_B";
  public static final String CHIKOU = "CHIKOU";

  public static final String EMA = "EMA";
  public static final String SMA = "SMA";
  public static final String RSI = "RSI";
}
