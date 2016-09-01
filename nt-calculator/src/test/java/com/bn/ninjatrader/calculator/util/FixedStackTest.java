package com.bn.ninjatrader.calculator.util;

import com.beust.jcommander.internal.Lists;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

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

  @Test
  public void testAddAll() {
    FixedStack<String> stack = FixedStack.withFixedSize(2);
    stack.addAll(Lists.newArrayList("A", "B", "C"));

    assertEquals(stack.size(), 2);
    assertEquals(stack.get(0), "B");
    assertEquals(stack.get(1), "C");

    stack.addAll(Lists.newArrayList("D", "E", "F"));

    assertEquals(stack.size(), 2);
    assertEquals(stack.get(0), "E");
    assertEquals(stack.get(1), "F");
  }

  @Test
  public void testHandleAddAndRemove() {
    MockedFixedStack stack = new MockedFixedStack(2);
    assertSizeEquals(stack.addedList, 0);
    assertSizeEquals(stack.removedList, 0);

    stack.add("A");
    assertSizeEquals(stack.addedList, 1);
    assertListContains(stack.addedList, "A");
    assertSizeEquals(stack.removedList, 0);

    stack.add("B");
    assertSizeEquals(stack.addedList, 2);
    assertListContains(stack.addedList, "A", "B");
    assertSizeEquals(stack.removedList, 0);

    stack.add("C");
    assertSizeEquals(stack.addedList, 3);
    assertListContains(stack.addedList, "A", "B", "C");
    assertSizeEquals(stack.removedList, 1);
    assertListContains(stack.removedList, "A");
  }

  @Test
  public void testHandleAddAndRemoveWithAddAll() {
    MockedFixedStack stack = new MockedFixedStack(2);

    stack.addAll(Lists.newArrayList("A", "B", "C"));
    assertSizeEquals(stack.addedList, 3);
    assertListContains(stack.addedList, "A", "B", "C");
    assertSizeEquals(stack.removedList, 1);
    assertListContains(stack.removedList, "A");

    stack.addAll(Lists.newArrayList("D", "E", "F"));
    assertSizeEquals(stack.addedList, 6);
    assertListContains(stack.addedList, "A", "B", "C", "D", "E", "F");
    assertSizeEquals(stack.removedList, 4);
    assertListContains(stack.removedList, "A", "B", "C", "D");
  }

  private void assertSizeEquals(List list, int size) {
    assertNotNull(list);
    assertEquals(list.size(), size);
  }

  private void assertListContains(List<String> list, String ... expectedValues) {
    for (String expectedValue : expectedValues) {
      assertTrue(list.contains(expectedValue), "List expected to contain [" + expectedValue+ "] but found none.");
    }
  }

  /**
   * Extend FixedStack to test handleAdd and handleRemoved methods.
   */
  private static class MockedFixedStack extends FixedStack<String> {
    private List<String> addedList = Lists.newArrayList();
    private List<String> removedList = Lists.newArrayList();

    private MockedFixedStack(int fixedSize) {
      super(fixedSize);
    }

    @Override
    protected void handleAfterAdd(String added) {
      addedList.add(added);
    }

    @Override
    protected void handleAfterRemove(String removed) {
      removedList.add(removed);
    }
  }
}
