package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/5/16.
 */
public class OrConditionTest {
  private static final Logger LOG = LoggerFactory.getLogger(OrConditionTest.class);

  private final Condition trueCondition = new TrueCondition();
  private final Condition falseCondition = new FalseCondition();
  private final BarData barData = new BarData();
  private final ObjectMapper om = TestUtil.objectMapper();

  @Test
  public void testNullCondition_shouldReturnTrue() {
    OrCondition condition = new OrCondition();
    assertThat(condition.isMatch(barData)).isTrue();
  }

  @Test
  public void testAllTrue_shouldReturnTrue() {
    OrCondition condition = new OrCondition(trueCondition, trueCondition);
    assertTrue(condition.isMatch(barData));
  }

  @Test
  public void testAllFalse_shouldReturnFalse() {
    OrCondition condition = new OrCondition(falseCondition, falseCondition);
    assertFalse(condition.isMatch(barData));
  }

  @Test
  public void testMixedTrueAndFalse_shouldReturnTrue() {
    OrCondition condition = new OrCondition(falseCondition, falseCondition, falseCondition, trueCondition);
    assertTrue(condition.isMatch(barData));

    condition = new OrCondition(falseCondition, trueCondition, trueCondition);
    assertTrue(condition.isMatch(barData));
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    OrCondition condition = new OrCondition()
        .add(Conditions.eq(PRICE_CLOSE, 1.0))
        .add(Conditions.eq(PRICE_OPEN, PRICE_LOW));
    String serialized = om.writeValueAsString(condition);
    Condition deserialized = om.readValue(serialized, Condition.class);

    assertThat(deserialized).isInstanceOf(OrCondition.class).isEqualTo(condition);
  }
}
