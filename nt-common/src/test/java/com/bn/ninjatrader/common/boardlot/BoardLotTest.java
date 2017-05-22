package com.bn.ninjatrader.common.boardlot;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/18/16.
 */
public class BoardLotTest {

  private BoardLot boardLot = BoardLot.newLot().min(0.0001).max(0.9999).tick(0.0001).lot(1000).decimalPlaces(4).build();

  @Test
  public void testCreate() {
    assertThat(boardLot.getMinPrice()).isEqualTo(0.0001);
    assertThat(boardLot.getMaxPrice()).isEqualTo(0.9999);
    assertThat(boardLot.getTick()).isEqualTo(0.0001);
    assertThat(boardLot.getLotSize()).isEqualTo(1000);
    assertThat(boardLot.getDecimalPlaces()).isEqualTo(4);
  }

  @Test
  public void testIsMatch() {
    assertThat(boardLot.isPriceMatch(0.0001)).isTrue();
    assertThat(boardLot.isPriceMatch(0.9999)).isTrue();
    assertThat(boardLot.isPriceMatch(0.000101)).isTrue();
    assertThat(boardLot.isPriceMatch(0.999899)).isTrue();

    assertThat(boardLot.isPriceMatch(0.000099)).isFalse();
    assertThat(boardLot.isPriceMatch(1)).isFalse();
  }
}
