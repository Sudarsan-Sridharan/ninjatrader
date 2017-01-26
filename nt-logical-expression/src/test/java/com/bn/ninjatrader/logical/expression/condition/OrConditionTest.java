package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/5/16.
 */
public class OrConditionTest {
  private static final Logger LOG = LoggerFactory.getLogger(OrConditionTest.class);

  private final Condition trueCondition = new TrueCondition();
  private final Condition falseCondition = new FalseCondition();
  private final Data data = new MockData();

  @Test
  public void testNullCondition_shouldReturnTrue() {
    OrCondition condition = new OrCondition();
    assertThat(condition.isMatch(data)).isTrue();
  }

  @Test
  public void testAllTrue_shouldReturnTrue() {
    OrCondition condition = new OrCondition(trueCondition, trueCondition);
    Assert.assertTrue(condition.isMatch(data));
  }

  @Test
  public void testAllFalse_shouldReturnFalse() {
    OrCondition condition = new OrCondition(falseCondition, falseCondition);
    Assert.assertFalse(condition.isMatch(data));
  }

  @Test
  public void testMixedTrueAndFalse_shouldReturnTrue() {
    OrCondition condition = new OrCondition(falseCondition, falseCondition, falseCondition, trueCondition);
    Assert.assertTrue(condition.isMatch(data));

    condition = new OrCondition(falseCondition, trueCondition, trueCondition);
    Assert.assertTrue(condition.isMatch(data));
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final OrCondition<Data> condition = new OrCondition<>()
        .add(Conditions.eq(Variable.of("DataType1"), 1.0))
        .add(Conditions.eq(Variable.of("DataType1"), Variable.of("DataType2")));
    final String serialized = om.writeValueAsString(condition);
    final Condition deserialized = om.readValue(serialized, Condition.class);

    assertThat(deserialized).isInstanceOf(OrCondition.class).isEqualTo(condition);
  }
}
