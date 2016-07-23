package com.bn.ninjatrader.common.function;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.function.handler.IchimokuChikouShiftBackwardHandler;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 6/1/16.
 */
public class ShiftBackwardTest {

  private IchimokuChikouShiftBackwardHandler handler = new IchimokuChikouShiftBackwardHandler();

  @Test
  public void testShiftBackwardBy1Period() {
    List<Ichimoku> list = createIchimokuList();

    ShiftBackward.forValues(list)
        .period(1)
        .handler(handler)
        .execute();

    assertEquals(list.size(), 3);

    assertEquals(list.get(0).getChikou(), 2d);
    assertEquals(list.get(0).getTenkan(), 1d);

    assertEquals(list.get(1).getChikou(), 3d);
    assertEquals(list.get(1).getTenkan(), 2d);

    assertEquals(list.get(2).getChikou(), 0d);
    assertEquals(list.get(2).getTenkan(), 3d);
  }

  @Test
  public void testShiftBackwardBy2Periods() {
    List<Ichimoku> list = createIchimokuList();

    ShiftBackward.forValues(list)
        .period(2)
        .handler(handler)
        .execute();

    assertEquals(list.size(), 3);
    assertEquals(list.get(0).getChikou(), 3d);
    assertEquals(list.get(0).getTenkan(), 1d);

    assertEquals(list.get(1).getChikou(), 0d);
    assertEquals(list.get(1).getTenkan(), 2d);

    assertEquals(list.get(2).getChikou(), 0d);
    assertEquals(list.get(2).getTenkan(), 3d);
  }

  private List<Ichimoku> createIchimokuList() {
    Ichimoku ichimoku1 = new Ichimoku();
    ichimoku1.setChikou(1);
    ichimoku1.setTenkan(1);

    Ichimoku ichimoku2 = new Ichimoku();
    ichimoku2.setChikou(2);
    ichimoku2.setTenkan(2);

    Ichimoku ichimoku3 = new Ichimoku();
    ichimoku3.setChikou(3);
    ichimoku3.setTenkan(3);

    List<Ichimoku> list = Lists.newArrayList();
    list.add(ichimoku1);
    list.add(ichimoku2);
    list.add(ichimoku3);

    return list;
  }
}
