package com.bn.ninjatrader.model.data;

import com.bn.ninjatrader.common.data.Ichimoku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 5/2/16.
 */
public class IchimokuDataTest {

  private static final Logger log = LoggerFactory.getLogger(IchimokuDataTest.class);

  private Random random = new Random();

  @Test
  public void testFullOverlap() {
    LocalDate date = LocalDate.of(2016, 1, 1);

    // Original Data
    Ichimoku ichimoku = createIchimoku(date, 1d, 1d, 1d);
    IchimokuData ichimokuData = new IchimokuData("MEG", 2016);
    ichimokuData.getData().add(ichimoku);

    // Overlap Data
    ichimoku = createIchimoku(date, 2d, 2d, 2d);
    IchimokuData ichimokuDataOverlap = new IchimokuData("MEG", 2016);
    ichimokuDataOverlap.getData().add(ichimoku);

    ichimokuData.overlapWith(ichimokuDataOverlap);

    assertEquals(ichimokuData.getData().size(), 1);

    ichimoku = ichimokuData.getData().get(0);
    assertEquals(ichimoku.getDate(), date);
    assertEquals(ichimoku.getChikou(), 2d);
    assertEquals(ichimoku.getTenkan(), 2d);
    assertEquals(ichimoku.getKijun(), 2d);
    assertEquals(ichimoku.getSenkouA(), 2d);
    assertEquals(ichimoku.getSenkouB(), 2d);
  }

  @Test
  public void testPartialOverlap() {
    LocalDate date = LocalDate.of(2016, 1, 1);

    // Original Data
    Ichimoku ichimoku = createIchimoku(date, 0d, 1d, 1d);
    IchimokuData ichimokuData = new IchimokuData("MEG", 2016);
    ichimokuData.getData().add(ichimoku);

    // Overlap Data
    ichimoku = createIchimoku(date, 2d, 2d, 0d);
    IchimokuData ichimokuDataOverlap = new IchimokuData("MEG", 2016);
    ichimokuDataOverlap.getData().add(ichimoku);

    ichimokuData.overlapWith(ichimokuDataOverlap);

    assertEquals(ichimokuData.getData().size(), 1);

    ichimoku = ichimokuData.getData().get(0);
    assertEquals(ichimoku.getDate(), date);
    assertEquals(ichimoku.getChikou(), 2d);
    assertEquals(ichimoku.getTenkan(), 2d);
    assertEquals(ichimoku.getKijun(), 2d);
    assertEquals(ichimoku.getSenkouA(), 1d);
    assertEquals(ichimoku.getSenkouB(), 1d);
  }

  private Ichimoku createIchimoku(LocalDate date, double chikou, double tk, double senkou) {
    Ichimoku ichimoku = new Ichimoku();
    ichimoku.setDate(date);
    ichimoku.setChikou(chikou);
    ichimoku.setTenkan(chikou);
    ichimoku.setKijun(chikou);
    ichimoku.setSenkouA(senkou);
    ichimoku.setSenkouB(senkou);
    return ichimoku;
  }
}
