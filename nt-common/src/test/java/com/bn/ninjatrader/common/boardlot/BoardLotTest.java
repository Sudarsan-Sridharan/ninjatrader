package com.bn.ninjatrader.common.boardlot;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/18/16.
 */
public class BoardLotTest {

  private BoardLot boardLot = BoardLot.newLot().min(0.0001).max(0.9999).tick(0.0001).lot(1000).build();

  @Test
  public void testCreate() {
    assertEquals(boardLot.getMinPrice(), 0.0001);
    assertEquals(boardLot.getMaxPrice(), 0.9999);
    assertEquals(boardLot.getTick(), 0.0001);
    assertEquals(boardLot.getLotSize(), 1000);
  }

  @Test
  public void testIsMatch() {
    assertTrue(boardLot.isPriceMatch(0.0001));
    assertTrue(boardLot.isPriceMatch(0.9999));
    assertTrue(boardLot.isPriceMatch(0.000101));
    assertTrue(boardLot.isPriceMatch(0.999899));

    assertFalse(boardLot.isPriceMatch(0.000099));
    assertFalse(boardLot.isPriceMatch(1));
  }
}
