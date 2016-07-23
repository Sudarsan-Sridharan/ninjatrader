package com.bn.ninjatrader.calculator.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 7/21/16.
 */
public class FixedStackTest {

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testWithSizeZero() {
    FixedStack<String> stack = FixedStack.withFixedSize(0);
  }

  @Test
  public void testAdd() {
    FixedStack<String> stack = FixedStack.withFixedSize(2);
    assertEquals(stack.size(), 0);

    stack.add("A");
    assertEquals(stack.size(), 1);
    assertEquals(stack.get(0), "A");

    stack.add("B");
    assertEquals(stack.size(), 2);
    assertEquals(stack.get(1), "B");

    stack.add("C");
    assertEquals(stack.size(), 2);
    assertEquals(stack.get(0), "B");
    assertEquals(stack.get(1), "C");
  }
}
