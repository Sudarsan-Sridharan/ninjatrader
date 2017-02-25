package com.bn.ninjatrader.model.util;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class IdGeneratorTest {

  private IdGenerator idGenerator = new IdGenerator();

  @Test
  public void testIdLength() {
    assertThat(idGenerator.createId()).hasSize(20);
  }

  @Test
  public void testIdIsUnique() {
    Set<String> ids = Sets.newHashSet();
    for (int i = 0; i < 1000; i++) {
      ids.add(idGenerator.createId());
    }
    assertThat(ids).hasSize(1000);
  }
}
