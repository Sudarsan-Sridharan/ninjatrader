package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/5/16.
 */
public class MultiConditionTest {
  private static final Logger LOG = LoggerFactory.getLogger(MultiConditionTest.class);

  private final Condition trueCondition = new TrueCondition();
  private final Condition falseCondition = new FalseCondition();
  private final BarData barParameters = new BarData();
  private final ObjectMapper om = TestUtil.objectMapper();

  @Test
  public void testAllTrue_shouldReturnTrue() {
    OrCondition condition = new OrCondition(trueCondition, trueCondition);
    assertTrue(condition.isMatch(barParameters));
  }

  @Test
  public void testAllFalse_shouldReturnFalse() {
    OrCondition condition = new OrCondition(falseCondition, falseCondition);
    assertFalse(condition.isMatch(barParameters));
  }

  @Test
  public void testMixedTrueAndFalse_shouldReturnTrue() {
    OrCondition condition = new OrCondition(falseCondition, falseCondition, falseCondition, trueCondition);
    assertTrue(condition.isMatch(barParameters));

    condition = new OrCondition(falseCondition, trueCondition, trueCondition);
    assertTrue(condition.isMatch(barParameters));
  }

  @Test
  public void testGetVariables_shouldReturnAllVariablesInAllConditions() {
    OrCondition condition = new OrCondition();
    condition.add(Conditions.eq(PRICE_CLOSE, 12));
    condition.add(Conditions.eq(PRICE_OPEN, PRICE_LOW));
    assertThat(condition.getVariables()).hasSize(3).containsOnly(PRICE_CLOSE, PRICE_OPEN, PRICE_LOW);
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

  @Test
  public void testEqualsWithSameConditions_shouldReturnEqual() {
    OrCondition condition = new OrCondition()
        .add(Conditions.eq(PRICE_CLOSE, 1.0))
        .add(Conditions.eq(PRICE_OPEN, PRICE_LOW));
    OrCondition equalCondition = new OrCondition()
        .add(Conditions.eq(PRICE_CLOSE, 1.0))
        .add(Conditions.eq(PRICE_OPEN, PRICE_LOW));

    assertThat(condition).isEqualTo(equalCondition);
    assertThat(equalCondition).isEqualTo(condition);
  }

  @Test
  public void testEqualsWithDiffConditions_shouldReturnNotEqual() {
    OrCondition orig = new OrCondition().add(Conditions.eq(PRICE_CLOSE, 1.0));
    OrCondition diff1 = new OrCondition().add(Conditions.eq(PRICE_CLOSE, 1.1));
    OrCondition diff2 = new OrCondition().add(Conditions.eq(PRICE_CLOSE, 1.0)).add(trueCondition);

    assertThat(orig).isNotEqualTo(diff1);
    assertThat(orig).isNotEqualTo(diff2);
  }

  @Test
  public void testHashCode_shouldHaveSameHashCodesForEqualObjects() {
    Set<OrCondition> set = Sets.newHashSet();
    set.add(new OrCondition().add(Conditions.eq(PRICE_CLOSE, 1.0)));
    set.add(new OrCondition().add(Conditions.eq(PRICE_CLOSE, 1.0)));
    set.add(new OrCondition().add(Conditions.eq(PRICE_CLOSE, 1.1)));
    set.add(new OrCondition().add(Conditions.eq(PRICE_CLOSE, 1.0)).add(trueCondition));

    assertThat(set).hasSize(3);
  }
}
