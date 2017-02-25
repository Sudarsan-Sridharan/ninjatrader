package com.bn.ninjatrader.model.entity;

import com.beust.jcommander.internal.Sets;
import com.bn.ninjatrader.model.deprecated.Report;
import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Brad on 7/27/16.
 */
public class ReportTest {

  private Report orig = new Report("report_id", "user_id", "sample_report_data");

  private Report sameReport = new Report("report_id", "user_id", "sample_report_data");

  private Report diffReport1 = new Report("X", "user_id", "sample_report_data");
  private Report diffReport2 = new Report("report_id", "X", "sample_report_data");
  private Report diffReport3 = new Report("report_id", "user_id", "X");

  @Test
  public void testEqualsWithSameObject() {
    assertTrue(orig.equals(orig));
  }

  @Test
  public void testEqualsWithSameValue() {
    assertTrue(orig.equals(sameReport));
    assertTrue(sameReport.equals(orig));
  }

  @Test
  public void testEqualsWithDifferences() {
    assertFalse(orig.equals(diffReport1));
    assertFalse(orig.equals(diffReport2));
    assertFalse(orig.equals(diffReport3));
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
    assertEquals(orig.hashCode(), sameReport.hashCode());
    assertNotEquals(orig.hashCode(), diffReport1.hashCode());
    assertNotEquals(orig.hashCode(), diffReport2.hashCode());
    assertNotEquals(orig.hashCode(), diffReport3.hashCode());
  }

  @Test
  public void testHashCodeWithSet() {
    Set<Report> valueSet = Sets.newHashSet();

    // Add same object
    valueSet.add(orig);
    valueSet.add(orig);
    assertEquals(valueSet.size(), 1);

    // Add different object with same values
    valueSet.add(sameReport);
    assertEquals(valueSet.size(), 1);

    // Add Value object with different value
    valueSet.add(diffReport1);
    assertEquals(valueSet.size(), 2);

    // Add Value with different date
    valueSet.add(diffReport2);
    assertEquals(valueSet.size(), 3);
  }
}
