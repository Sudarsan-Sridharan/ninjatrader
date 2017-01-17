package com.bn.ninjatrader.model.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author bradwee2000@gmail.com
 */
public class QueriesTest {

  @Test
  public void testCreateQuery() {
    assertEquals(Queries.createQuery("A"), "{A:#}");
    assertEquals(Queries.createQuery("A", "B"), "{A:#, B:#}");
    assertEquals(Queries.createQuery("A", "B", "C", "D"), "{A:#, B:#, C:#, D:#}");
  }

  @Test
  public void testCreateIndex() {
    assertEquals(Queries.createIndex("A"), "{A:1}");
    assertEquals(Queries.createIndex("A", "B"), "{A:1, B:1}");
    assertEquals(Queries.createIndex("A", "B", "C", "D"), "{A:1, B:1, C:1, D:1}");
  }
}
