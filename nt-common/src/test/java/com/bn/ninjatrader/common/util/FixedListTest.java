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
  public void testFixedListAddAtIndex() {
    FixedList<Integer> fixedList = new FixedList<Integer>(2);

    fixedList.add(0, 2);
    assertEquals(fixedList.size(), 1);
    assertEquals(fixedList.get(0).intValue(), 2);

    fixedList.add(0, 1);
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 1);
    assertEquals(fixedList.get(1).intValue(), 2);

    fixedList.add(0, 0);
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 1);
    assertEquals(fixedList.get(1).intValue(), 2);

    fixedList.add(2, 10);
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 2);
    assertEquals(fixedList.get(1).intValue(), 10);
  }

  @Test
  public void testFixedListAddAll() {
    FixedList<Integer> fixedList = FixedList.withMaxSize(2);

    fixedList.addAll(Arrays.asList(1, 2, 3, 4));
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 3);

    fixedList.addAll(Arrays.asList(5, 6 ,7));
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 6);
  }

  @Test
  public void testFixedListAddAllAtIndex() {
    FixedList<Integer> fixedList = FixedList.withMaxSize(2);

    fixedList.addAll(Arrays.asList(5, 6, 7));
    fixedList.addAll(0, Arrays.asList(2, 3, 4));

    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 6);
    assertEquals(fixedList.get(1).intValue(), 7);

    fixedList.addAll(2, Arrays.asList(99, 100));
    assertEquals(fixedList.size(), 2);
    assertEquals(fixedList.get(0).intValue(), 99);
    assertEquals(fixedList.get(1).intValue(), 100);
  }
}
