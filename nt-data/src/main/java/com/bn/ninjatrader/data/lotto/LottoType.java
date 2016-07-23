package com.bn.ninjatrader.data.lotto;

/**
 * Created by Brad on 6/26/16.
 */
public enum LottoType {

  LOTTO42(42, "/Users/a-/Work/NinjaTrader/nt-data/src/main/java/com/bn/ninjatrader/data/lotto/lotto42.csv"),
  LOTTO45(45, "/Users/a-/Work/NinjaTrader/nt-data/src/main/java/com/bn/ninjatrader/data/lotto/lotto45.csv"),
  LOTTO49(49, "/Users/a-/Work/NinjaTrader/nt-data/src/main/java/com/bn/ninjatrader/data/lotto/lotto49.csv"),
  LOTTO55(55, "/Users/a-/Work/NinjaTrader/nt-data/src/main/java/com/bn/ninjatrader/data/lotto/lotto55.csv");

  private int max;
  private String filePath;

  private LottoType(int max, String filePath) {
    this.max = max;
    this.filePath = filePath;
  }

  public int max() {
    return max;
  }

  public String path() {
    return filePath;
  }
}
