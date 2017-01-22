package com.bn.ninjatrader.simulation.condition;

import com.beust.jcommander.internal.Sets;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Constant;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.type.InequalityOperator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_OPEN;
import static com.bn.ninjatrader.simulation.type.InequalityOperator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/2/16.
 */
public class BasicConditionTest {
  private static final Logger LOG = LoggerFactory.getLogger(BasicConditionTest.class);

  private final BarData barData = BarData.builder().build();
  private final ObjectMapper om = TestUtil.objectMapper();

  @Test
  public void testEqualsCondition_shouldReturnTrueIfEqual() {
    assertThat(isConditionMatch(0, EQ, -0)).isTrue();
    assertThat(isConditionMatch(4.2345, EQ, 4.2345)).isTrue();
    assertThat(isConditionMatch(4.2345, EQ, 4.23450)).isTrue();
    assertThat(isConditionMatch(4.2345, EQ, 4.23456)).isFalse();
  }

  @Test
  public void testGreaterThanCondition_shouldReturnTrueIfGreater() {
    assertThat(isConditionMatch(4.2345, GT, 4.2344)).isTrue();
    assertThat(isConditionMatch(4.2345, GT, 4.2345)).isFalse();
    assertThat(isConditionMatch(4.2345, GT, 5.0)).isFalse();
  }

  @Test
  public void testLessThanCondition_shouldReturnTrueIfLess() {
    assertThat(isConditionMatch(4.2345, LT, 4.2346)).isTrue();
    assertThat(isConditionMatch(-4.2345, LT, -4.2344)).isTrue();
    assertThat(isConditionMatch(4.2345, LT, 4.2345)).isFalse();
    assertThat(isConditionMatch(4.2345, LT, 3.0)).isFalse();
  }

  @Test
  public void testGetVariablesOfConstantCondition_shouldReturnZeroVariables() {
    Condition condition = new BasicCondition(Constant.of(3.0), GT, Constant.of(1.0));
    assertThat(condition.getVariables()).hasSize(0);
  }

  @Test
  public void testGetVariablesOfVariableCondition_shouldReturnAllVariables() {
    Condition condition = new BasicCondition(PRICE_CLOSE, GT, PRICE_OPEN);
    assertThat(condition.getVariables()).containsOnly(PRICE_OPEN, PRICE_CLOSE);
  }

  @Test
  public void testEqualsWithSameObjects_shouldReturnEqual() {
    assertThat(new BasicCondition(PRICE_CLOSE, GT, PRICE_OPEN))
        .isEqualTo(new BasicCondition(PRICE_CLOSE, GT, PRICE_OPEN));
    assertThat(new BasicCondition(PRICE_CLOSE, EQ, Constant.of(100)))
        .isEqualTo(new BasicCondition(PRICE_CLOSE, EQ, Constant.of(100)));
    assertThat(new BasicCondition(Constant.of(555), LT, Constant.of(100)))
        .isEqualTo(new BasicCondition(Constant.of(555), LT, Constant.of(100)));
  }

  @Test
  public void testEqualsWithDiffObjects_shouldReturnNotEqual() {
    assertThat(new BasicCondition(PRICE_CLOSE, GT, PRICE_OPEN))
        .isNotEqualTo(new BasicCondition(PRICE_CLOSE, GT, PRICE_CLOSE));
    assertThat(new BasicCondition(PRICE_CLOSE, EQ, Constant.of(100)))
        .isNotEqualTo(new BasicCondition(PRICE_CLOSE, EQ, Constant.of(101)));
    assertThat(new BasicCondition(Constant.of(555), LT, Constant.of(100)))
        .isNotEqualTo(new BasicCondition(Constant.of(444), LT, Constant.of(100)));
  }

  @Test
  public void testHashCode_shouldReturnDiffHashCodesForDiffObjects() {
    Set<BasicCondition> set = Sets.newHashSet();
    set.add(new BasicCondition(PRICE_CLOSE, GT, PRICE_OPEN));
    set.add(new BasicCondition(PRICE_CLOSE, GT, PRICE_OPEN)); // Add duplicate
    set.add(new BasicCondition(PRICE_CLOSE, EQ, Constant.of(100)));
    set.add(new BasicCondition(Constant.of(555), LT, Constant.of(100)));
    set.add(new BasicCondition(Constant.of(555), LT, Constant.of(100))); // Add duplicate

    assertThat(set).hasSize(3);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnSameObject() throws IOException {
    Condition condition = new BasicCondition(Constant.of(3.0), GT, Constant.of(1.0));
    String serialized = om.writeValueAsString(condition);
    Condition deserialised = om.readValue(serialized, Condition.class);
    assertThat(deserialised).isEqualTo(condition);
  }

  /**
   * Creates a basic condition and checks if it matches.
   * @param lhs Left-hand-side operation
   * @param operator Inequality operator
   * @param rhs Right-hand-side operation
   * @return true if condition matches.
   */
  private boolean isConditionMatch(double lhs, InequalityOperator operator, double rhs) {
    return isConditionMatch(Constant.of(lhs), operator, Constant.of(rhs));
  }

  /**
   * Creates a basic condition and checks if it matches.
   * @param lhs Left-hand-side operation
   * @param operator Inequality operator
   * @param rhs Right-hand-side operation
   * @return true if condition matches.
   */
  private boolean isConditionMatch(Operation lhs, InequalityOperator operator, Operation rhs) {
    return new BasicCondition(lhs, operator, rhs).isMatch(barData);
  }
}
