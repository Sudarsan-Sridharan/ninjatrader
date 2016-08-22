package com.bn.ninjatrader.common.boardlot;

import com.bn.ninjatrader.common.util.NumUtil;

/**
 * Created by Brad on 8/18/16.
 */
public class BoardLot {

  private final double minPrice;
  private final double maxPrice;
  private final double tick;
  private final int lotSize;

  public static BoardLotEntryBuilder newLot() {
    return new BoardLotEntryBuilder();
  }

  private BoardLot(double minPrice, double maxPrice, double tick, int lotSize) {
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
    this.tick = tick;
    this.lotSize = lotSize;
  }

  public double getMinPrice() {
    return minPrice;
  }

  public double getMaxPrice() {
    return maxPrice;
  }

  public double getTick() {
    return tick;
  }

  public int getLotSize() {
    return lotSize;
  }

  public boolean isPriceMatch(double price) {
    price = NumUtil.trimPrice(price);
    return price >= minPrice && price <= maxPrice;
  }

  public static class BoardLotEntryBuilder {
    private double minPrice;
    private double maxPrice;
    private double tick;
    private int lotSize;

    public BoardLotEntryBuilder min(double minPrice) {
      this.minPrice = minPrice;
      return this;
    }

    public BoardLotEntryBuilder max(double maxPrice) {
      this.maxPrice = maxPrice;
      return this;
    }

    public BoardLotEntryBuilder tick(double tick) {
      this.tick = tick;
      return this;
    }

    public BoardLotEntryBuilder lot(int lotSize) {
      this.lotSize = lotSize;
      return this;
    }

    public BoardLot build() {
      return new BoardLot(minPrice, maxPrice, tick, lotSize);
    }
  }
}
