package com.bn.ninjatrader.common.util;

import com.beust.jcommander.internal.Lists;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/18/16.
 */
public class ListUtilTest {

  @Test
  public void testFillToSize() {
    List<String> list = Lists.newArrayList();

    ListUtil.fillToSize(list, "filler", 1);
    assertEquals(list.size(), 1);
    assertEquals(list.get(0), "filler");

    ListUtil.fillToSize(list, "", 10);
    assertEquals(list.size(), 10);
    assertEquals(list.get(0), "");
    assertEquals(list.get(9), "filler");

    ListUtil.fillToSize(list, "", 1);
    assertEquals(list.size(), 10);
    assertEquals(list.get(0), "");
    assertEquals(list.get(9), "filler");
  }
}
