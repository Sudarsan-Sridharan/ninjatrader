package com.bn.ninjatrader.common.boardlot;

import com.bn.ninjatrader.common.util.NumUtil;
import com.google.common.base.MoreObjects;

/**
 * Created by Brad on 8/18/16.
 */
public class BoardLot {

  private final double minPrice;
  private final double maxPrice;
  private final double tick;
  private final int lotSize;
  private final int decimalPlaces;

  public static BoardLotEntryBuilder newLot() {
    return new BoardLotEntryBuilder();
  }

  private BoardLot(final double minPrice,
                   final double maxPrice,
                   final double tick,
                   final int lotSize,
                   final int decimalPlaces) {
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
    this.tick = tick;
    this.lotSize = lotSize;
    this.decimalPlaces = decimalPlaces;
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

  public int getDecimalPlaces() {
    return decimalPlaces;
  }

  public boolean isPriceMatch(double price) {
    price = NumUtil.trimPrice(price);
    return price >= minPrice && price <= maxPrice;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("minPrice", minPrice)
        .add("maxPrice", maxPrice)
        .add("tick", tick)
        .add("lotSize", lotSize)
        .add("decimalPlaces", decimalPlaces)
        .toString();
  }

  /**
   * Builder
   */
  public static class BoardLotEntryBuilder {
    private double minPrice;
    private double maxPrice;
    private double tick;
    private int lotSize;
    private int decimalPlaces;

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

    public BoardLotEntryBuilder decimalPlaces(int decimalPlaces) {
      this.decimalPlaces = decimalPlaces;
      return this;
    }

    public BoardLot build() {
      return new BoardLot(minPrice, maxPrice, tick, lotSize, decimalPlaces);
    }
  }
}
