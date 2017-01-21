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

/**
 * Created by Brad on 8/5/16.
 */
public class AndConditionTest {
  private static final Logger LOG = LoggerFactory.getLogger(AndConditionTest.class);

  private final Condition trueCondition = new TrueCondition();
  private final Condition falseCondition = new FalseCondition();
  private final BarData barData = BarData.builder().build();
  private final ObjectMapper om = TestUtil.objectMapper();

  @Test
  public void testNullCondition_shouldReturnTrue() {
    AndCondition condition = new AndCondition();
    assertThat(condition.isMatch(barData)).isTrue();
  }

  @Test
  public void testAllTrue_shouldReturnTrue() {
    AndCondition condition = new AndCondition(trueCondition, trueCondition);
    assertThat(condition.isMatch(barData)).isTrue();
  }

  @Test
  public void testAllFalse_shouldReturnFalse() {
    AndCondition condition = new AndCondition(falseCondition, falseCondition);
    assertThat(condition.isMatch(barData)).isFalse();
  }

  @Test
  public void testMixedTrueAndFalse_shouldReturnFalse() {
    AndCondition condition = new AndCondition(trueCondition, trueCondition, trueCondition, falseCondition);
    assertThat(condition.isMatch(barData)).isFalse();

    condition = new AndCondition(falseCondition, trueCondition, trueCondition);
    assertThat(condition.isMatch(barData)).isFalse();
  }

  @Test
  public void testAddCondition_shouldReturnFalseIfOneIsFalse() {
    AndCondition condition = new AndCondition();
    condition.add(trueCondition);
    condition.add(trueCondition);
    assertThat(condition.isMatch(barData)).isTrue();

    condition.add(falseCondition);
    assertThat(condition.isMatch(barData)).isFalse();
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    AndCondition condition = new AndCondition()
        .add(Conditions.eq(PRICE_CLOSE, 1.0))
        .add(Conditions.eq(PRICE_OPEN, PRICE_LOW));
    String serialized = om.writeValueAsString(condition);
    Condition deserialized = om.readValue(serialized, Condition.class);
    assertThat(deserialized).isInstanceOf(AndCondition.class).isEqualTo(condition);
  }
}
