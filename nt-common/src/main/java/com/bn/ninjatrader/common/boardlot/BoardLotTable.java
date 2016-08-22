package com.bn.ninjatrader.common.boardlot;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static com.bn.ninjatrader.common.boardlot.BoardLot.newLot;

/**
 * Created by Brad on 8/18/16.
 */
@Singleton
public class BoardLotTable {

  private static final List<BoardLot> STANDARD_BOARD_LOTS = Collections.unmodifiableList(Lists.newArrayList(
      newLot().min(0.0001).max(0.0099).tick(0.0001).lot(1000000).build(),
      newLot().min(0.0100).max(0.0499).tick(0.0010).lot(100000).build(),
      newLot().min(0.0500).max(0.2499).tick(0.0010).lot(10000).build(),
      newLot().min(0.2500).max(0.4999).tick(0.0050).lot(10000).build(),
      newLot().min(0.5000).max(4.9999).tick(0.0100).lot(1000).build(),
      newLot().min(5.0000).max(9.9999).tick(0.0100).lot(100).build(),
      newLot().min(10.0000).max(19.9999).tick(0.0200).lot(100).build(),
      newLot().min(20.0000).max(49.9999).tick(0.0500).lot(100).build(),
      newLot().min(50.0000).max(99.9999).tick(0.0500).lot(10).build(),
      newLot().min(100.0000).max(199.9999).tick(0.1000).lot(10).build(),
      newLot().min(200.0000).max(499.9999).tick(0.2000).lot(10).build(),
      newLot().min(500.0000).max(999.9999).tick(0.5000).lot(10).build(),
      newLot().min(1000.0000).max(1999.9999).tick(1.0000).lot(5).build(),
      newLot().min(2000.0000).max(4999.9999).tick(2.0000).lot(5).build(),
      newLot().min(5000.0000).max(99999.9999).tick(5.0000).lot(5).build()
  ));

  public  BoardLot getBoardLot(double price) {
    for (BoardLot entry : STANDARD_BOARD_LOTS) {
      if (entry.isPriceMatch(price)) {
        return entry;
      }
    }
    throw new IllegalStateException("Boardlot not found for price: " + price);
  }
}
