package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.logical.expression.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class ConstantTest {

  private static final Logger LOG = LoggerFactory.getLogger(ConstantTest.class);

  private final ObjectMapper om = TestUtil.om();

  @Test
  public void testEqualsWithSameValue_shouldReturnEqual() {
    assertThat(Constant.of(0)).isEqualTo(Constant.of(000));
    assertThat(Constant.of(1.00001)).isEqualTo(Constant.of(1.00001));
    assertThat(Constant.of(-900000000)).isEqualTo(Constant.of(-900000000));
    assertThat(Constant.of(900000000)).isEqualTo(Constant.of(900000000));
  }

  @Test
  public void testEqualsWithDiffValue_shouldReturnNotEqual() {
    assertThat(Constant.of(0)).isNotEqualTo(Constant.of(0.01));
    assertThat(Constant.of(1.00001)).isNotEqualTo(Constant.of(-1.00001));
    assertThat(Constant.of(-900000000)).isNotEqualTo(Constant.of(-900000001));
    assertThat(Constant.of(900000000)).isNotEqualTo(Constant.of(900000001));
  }

  @Test
  public void testHashCode_shouldHaveDiffHashCodes() {
    Set<Constant> set = Sets.newHashSet();
    set.add(Constant.of(0));
    set.add(Constant.of(0));
    set.add(Constant.of(0.1));
    set.add(Constant.of(-0.1));
    assertThat(set).hasSize(3).containsOnly(Constant.of(0), Constant.of(0.1), Constant.of(-0.1));
  }

  @Test
  public void testSerializeDeserialize_shouldReturnSameObject() throws IOException {
    Constant constant = Constant.of(12345.6789);
    String serialized = om.writeValueAsString(constant);
    Constant deserialized = om.readValue(serialized, Constant.class);
    assertThat(deserialized).isEqualTo(constant);
  }
}
