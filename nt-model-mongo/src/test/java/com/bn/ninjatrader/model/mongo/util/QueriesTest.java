package com.bn.ninjatrader.model.mongo.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class QueriesTest {

  @Test
  public void testCreateQuery() {
    assertThat(Queries.createQuery("A")).isEqualTo("{A:#}");
    assertThat(Queries.createQuery("A", "B")).isEqualTo("{A:#, B:#}");
    assertThat(Queries.createQuery("A", "B", "C", "D")).isEqualTo("{A:#, B:#, C:#, D:#}");
  }

  @Test
  public void testCreateIndex() {
    assertThat(Queries.createIndex("A")).isEqualTo("{A:1}");
    assertThat(Queries.createIndex("A", "B")).isEqualTo("{A:1, B:1}");
    assertThat(Queries.createIndex("A", "B", "C", "D")).isEqualTo("{A:1, B:1, C:1, D:1}");
  }
}
