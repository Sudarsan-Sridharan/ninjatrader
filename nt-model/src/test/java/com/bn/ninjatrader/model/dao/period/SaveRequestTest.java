package com.bn.ninjatrader.model.dao.period;

import com.beust.jcommander.internal.Sets;
import com.bn.ninjatrader.common.data.Value;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Brad on 7/27/16.
 */
public class SaveRequestTest {

  private LocalDate date = LocalDate.of(2016, 1, 1);
  private Value value1 = Value.of(date, 1d);
  private Value value2 = Value.of(date, 2d);

  private SaveRequest orig = SaveRequest.save("MEG").period(1).values(value1);
  private SaveRequest same = SaveRequest.save("MEG").period(1).values(value1);

  private SaveRequest differentSymbol = SaveRequest.save("BDO").period(1).values(value1);
  private SaveRequest differentPeriod = SaveRequest.save("MEG").period(2).values(value1);

  private SaveRequest differentValue = SaveRequest.save("MEG").period(1).values(value2);
  private SaveRequest differentValueSize = SaveRequest.save("MEG").period(1).values(value1, value1);

  @Test
  public void testEqualsWithSameObject() {
    assertTrue(orig.equals(orig));
  }

  @Test
  public void testEqualsWithSameValue() {
    assertTrue(orig.equals(same));
    assertTrue(same.equals(orig));
  }

  @Test
  public void testEqualsWithDifferences() {
    assertFalse(orig.equals(differentSymbol));
    assertFalse(orig.equals(differentPeriod));
    assertFalse(orig.equals(differentValue));
    assertFalse(orig.equals(differentValueSize));
  }

  @Test
  public void testEqualsWithDiffObjectType() {
    assertFalse(orig.equals(new Object()));
    assertFalse(orig.equals("Wrong Object"));
    assertFalse(orig.equals(1));
    assertFalse(orig.equals(null));
  }

  @Test
  public void testHashCode() {
    assertNotEquals(orig.hashCode(), differentSymbol.hashCode());
    assertNotEquals(orig.hashCode(), differentPeriod.hashCode());
    assertNotEquals(orig.hashCode(), differentValue.hashCode());
    assertNotEquals(orig.hashCode(), differentValueSize.hashCode());

    assertEquals(orig.hashCode(), same.hashCode());
  }

  @Test
  public void testHashCodeWithSet() {
    Set<SaveRequest> set = Sets.newHashSet();

    set.add(orig);
    set.add(orig);
    assertEquals(set.size(), 1); // Should overwrite

    set.add(same);
    assertEquals(set.size(), 1); // Should overwrite

    set.add(differentSymbol);
    assertEquals(set.size(), 2);

    set.add(differentPeriod);
    assertEquals(set.size(), 3);
  }
}
