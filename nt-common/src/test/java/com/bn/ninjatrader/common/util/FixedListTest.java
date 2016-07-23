package com.bn.ninjatrader.common.util;

import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 5/28/16.
 */
public class FixedListTest {

  @Test
  public void testFixedListAdd() {
    FixedList<Integer> fixedList = new FixedList<Integer>(2);

    fixedList.add(1);
    assertEquals(fixedList.size(), 1);
    assertEquals(fixedList.get(0).intValue(), 1);

    fixedList.add(2);
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 1);

    fixedList.add(3);
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 2);

    fixedList.add(4);
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 3);
  }

  @Test
  public void testFixedListAddAll() {
    FixedList<Integer> fixedList = FixedList.newInstanceWithSize(2);

    fixedList.addAll(Arrays.asList(1, 2, 3, 4));
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 3);

    fixedList.addAll(Arrays.asList(5, 6 ,7));
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 6);
  }
}
