package com.bn.ninjatrader.simulation.operation;

import com.beust.jcommander.internal.Sets;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.DataType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class VariableTest {

  private static final Logger LOG = LoggerFactory.getLogger(VariableTest.class);

  private final ObjectMapper om = TestUtil.objectMapper();

  @Test
  public void testEqualsWithSameValue_shouldReturnEqual() {
    assertThat(Variable.of(DataType.PRICE_OPEN)).isEqualTo(Variable.of(DataType.PRICE_OPEN));
    assertThat(Variable.of(DataType.PRICE_HIGH)).isEqualTo(Variable.of(DataType.PRICE_HIGH));
    assertThat(Variable.of(DataType.SMA).period(21)).isEqualTo(Variable.of(DataType.SMA).period(21));
  }

  @Test
  public void testEqualsWithDiffValue_shouldReturnNotEqual() {
    assertThat(Variable.of(DataType.PRICE_OPEN)).isNotEqualTo(Variable.of(DataType.PRICE_CLOSE));
    assertThat(Variable.of(DataType.PRICE_HIGH)).isNotEqualTo(Variable.of(DataType.PRICE_LOW));
    assertThat(Variable.of(DataType.SMA).period(21)).isNotEqualTo(Variable.of(DataType.SMA).period(100));
  }

  @Test
  public void testHashCode_shouldHaveDiffHashCodes() {
    Set<Variable> set = Sets.newHashSet();
    set.add(Variable.of(DataType.PRICE_HIGH));
    set.add(Variable.of(DataType.PRICE_HIGH)); // add duplicate
    set.add(Variable.of(DataType.PRICE_LOW));
    set.add(Variable.of(DataType.SMA).period(21));
    set.add(Variable.of(DataType.SMA).period(21)); // add duplicate
    set.add(Variable.of(DataType.SMA).period(100));

    assertThat(set).hasSize(4);
    assertThat(set).containsOnly(
        Variable.of(DataType.PRICE_HIGH),
        Variable.of(DataType.PRICE_LOW),
        Variable.of(DataType.SMA).period(21),
        Variable.of(DataType.SMA).period(100));
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    Variable variable = Variable.of(DataType.SMA).period(200);
    String serialized = om.writeValueAsString(variable);
    Operation deserialized = om.readValue(serialized, Operation.class);
    assertThat(deserialized).isEqualTo(variable);
  }
}
