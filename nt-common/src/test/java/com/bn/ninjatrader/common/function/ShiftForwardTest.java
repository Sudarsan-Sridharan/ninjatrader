package com.bn.ninjatrader.common.function;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.function.handler.IchimokuSenkouShiftForwardHandler;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 6/1/16.
 */
public class ShiftForwardTest {

  private IchimokuSenkouShiftForwardHandler handler = new IchimokuSenkouShiftForwardHandler();

  @Test
  public void testShiftForwardBy0Period() {
    List<Ichimoku> list = createIchimokuList();

    ShiftForward.forValues(list)
        .period(0)
        .handler(handler)
        .execute();

    assertEquals(list.size(), 3);

    assertEquals(list.get(0).getSenkouA(), 1d);
    assertEquals(list.get(0).getSenkouB(), 10d);
    assertEquals(list.get(0).getTenkan(), 1d);

    assertEquals(list.get(1).getSenkouA(), 2d);
    assertEquals(list.get(1).getSenkouB(), 20d);
    assertEquals(list.get(1).getTenkan(), 2d);

    assertEquals(list.get(2).getSenkouA(), 3d);
    assertEquals(list.get(2).getSenkouB(), 30d);
    assertEquals(list.get(2).getTenkan(), 3d);
  }

  @Test
  public void testShiftForwardBy1Period() {
    List<Ichimoku> list = createIchimokuList();

    ShiftForward.forValues(list)
        .period(1)
        .handler(handler)
        .execute();

    assertEquals(list.size(), 4);
    assertEquals(list.get(0).getSenkouA(), 0d);
    assertEquals(list.get(0).getSenkouB(), 0d);
    assertEquals(list.get(0).getTenkan(), 1d);

    assertEquals(list.get(1).getSenkouA(), 1d);
    assertEquals(list.get(1).getSenkouB(), 10d);
    assertEquals(list.get(1).getTenkan(), 2d);

    assertEquals(list.get(2).getSenkouA(), 2d);
    assertEquals(list.get(2).getSenkouB(), 20d);
    assertEquals(list.get(2).getTenkan(), 3d);

    assertEquals(list.get(3).getSenkouA(), 3d);
    assertEquals(list.get(3).getSenkouB(), 30d);
    assertEquals(list.get(3).getTenkan(), 0d);
  }

  @Test
  public void testShiftForwardBy2Periods() {
    List<Ichimoku> list = createIchimokuList();

    ShiftForward.forValues(list)
        .period(2)
        .handler(handler)
        .execute();

    assertEquals(list.size(), 5);
    assertEquals(list.get(0).getSenkouA(), 0d);
    assertEquals(list.get(0).getTenkan(), 1d);

    assertEquals(list.get(1).getSenkouA(), 0d);
    assertEquals(list.get(1).getTenkan(), 2d);

    assertEquals(list.get(2).getSenkouA(), 1d);
    assertEquals(list.get(2).getTenkan(), 3d);

    assertEquals(list.get(3).getSenkouA(), 2d);
    assertEquals(list.get(3).getTenkan(), 0d);

    assertEquals(list.get(4).getSenkouA(), 3d);
    assertEquals(list.get(4).getTenkan(), 0d);
  }

  private List<Ichimoku> createIchimokuList() {
    Ichimoku ichimoku1 = new Ichimoku();
    ichimoku1.setSenkouA(1);
    ichimoku1.setSenkouB(10);
    ichimoku1.setTenkan(1);

    Ichimoku ichimoku2 = new Ichimoku();
    ichimoku2.setSenkouA(2);
    ichimoku2.setSenkouB(20);
    ichimoku2.setTenkan(2);

    Ichimoku ichimoku3 = new Ichimoku();
    ichimoku3.setSenkouA(3);
    ichimoku3.setSenkouB(30);
    ichimoku3.setTenkan(3);

    return Lists.newArrayList(ichimoku1, ichimoku2, ichimoku3);
  }
}
