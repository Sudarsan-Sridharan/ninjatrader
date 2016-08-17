package com.bn.ninjatrader.common.data;

import com.beust.jcommander.internal.Sets;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Brad on 7/27/16.
 */
public class ValueTest {

  private LocalDate date = LocalDate.of(2016, 1, 1);

  private Value origValue = Value.of(date, 1);
  private Value sameValue = Value.of(date, 1);

  private Value diffValue1 = Value.of(date, 2);
  private Value diffValue2 = Value.of(date, 1.0000001);

  private Value diffDayValue = Value.of(date.plusDays(1), 1);
  private Value diffMonthValue = Value.of(date.plusMonths(1), 1);
  private Value diffYearValue = Value.of(date.plusYears(1), 1);

  @Test
  public void testEqualsWithSameObject() {
    assertTrue(origValue.equals(origValue));
  }

  @Test
  public void testEqualsWithSameValue() {
    assertTrue(origValue.equals(sameValue));
    assertTrue(sameValue.equals(origValue));
  }

  @Test
  public void testEqualsWithDifferences() {
    assertFalse(origValue.equals(diffValue1));
    assertFalse(origValue.equals(diffValue2));

    assertFalse(origValue.equals(diffDayValue));
    assertFalse(origValue.equals(diffMonthValue));
    assertFalse(origValue.equals(diffYearValue));
  }

  @Test
  public void testEqualsWithDiffObjectType() {
    assertFalse(origValue.equals(new Object()));
    assertFalse(origValue.equals("Wrong Object"));
    assertFalse(origValue.equals(1));
    assertFalse(origValue.equals(null));
  }

  @Test
  public void testHashCode() {
    assertEquals(origValue.hashCode(), sameValue.hashCode());
    assertNotEquals(origValue.hashCode(), diffValue1.hashCode());
    assertNotEquals(origValue.hashCode(), diffDayValue.hashCode());
    assertNotEquals(origValue.hashCode(), diffMonthValue.hashCode());
    assertNotEquals(origValue.hashCode(), diffYearValue.hashCode());
  }

  @Test
  public void testHashCodeWithSet() {
    Set<Value> valueSet = Sets.newHashSet();

    // Add same object
    valueSet.add(origValue);
    valueSet.add(origValue);
    assertEquals(valueSet.size(), 1);

    // Add different object with same values
    valueSet.add(sameValue);
    assertEquals(valueSet.size(), 1);

    // Add Value object with different value
    valueSet.add(diffValue1);
    assertEquals(valueSet.size(), 2);

    // Add Value with different date
    valueSet.add(diffDayValue);
    assertEquals(valueSet.size(), 3);
  }
}
