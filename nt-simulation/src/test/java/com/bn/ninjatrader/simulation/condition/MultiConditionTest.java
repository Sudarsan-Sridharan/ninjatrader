package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.data.BarData;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Set;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/5/16.
 */
public class MultiConditionTest {
  private static final Logger LOG = LoggerFactory.getLogger(MultiConditionTest.class);

  @Test
  public void testCreate_shouldHaveZeroConditions() {
    DummyMultiCondition condition = new DummyMultiCondition();
    assertThat(condition.getConditions()).isNotNull().hasSize(0);
  }

  @Test
  public void testAddCondition_shouldAddConditionToList() {
    DummyMultiCondition condition = new DummyMultiCondition();
    condition.add(Conditions.eq(PRICE_CLOSE, 12));
    condition.add(Conditions.eq(PRICE_CLOSE, 12));
    assertThat(condition.getConditions()).hasSize(2);
  }

  @Test
  public void testGetVariables_shouldReturnAllVariablesInAllConditions() {
    DummyMultiCondition condition = new DummyMultiCondition();
    condition.add(Conditions.eq(PRICE_CLOSE, 12));
    condition.add(Conditions.eq(PRICE_OPEN, PRICE_LOW));
    assertThat(condition.getVariables()).hasSize(3).containsOnly(PRICE_CLOSE, PRICE_OPEN, PRICE_LOW);
  }

  @Test
  public void testEqualsWithSameConditions_shouldReturnEqual() {
    DummyMultiCondition condition = new DummyMultiCondition()
        .add(Conditions.eq(PRICE_CLOSE, 1.0))
        .add(Conditions.eq(PRICE_OPEN, PRICE_LOW));
    DummyMultiCondition equalCondition = new DummyMultiCondition()
        .add(Conditions.eq(PRICE_CLOSE, 1.0))
        .add(Conditions.eq(PRICE_OPEN, PRICE_LOW));

    assertThat(condition).isEqualTo(equalCondition);
    assertThat(equalCondition).isEqualTo(condition);
  }

  @Test
  public void testEqualsWithDiffConditions_shouldReturnNotEqual() {
    DummyMultiCondition orig = new DummyMultiCondition().add(Conditions.eq(PRICE_CLOSE, 1.0));
    DummyMultiCondition diff1 = new DummyMultiCondition().add(Conditions.eq(PRICE_CLOSE, 1.1));
    DummyMultiCondition diff2 = new DummyMultiCondition().add(Conditions.eq(PRICE_CLOSE, 1.0))
        .add(Conditions.gt(PRICE_HIGH, 4));

    assertThat(orig).isNotEqualTo(diff1);
    assertThat(orig).isNotEqualTo(diff2);
  }

  @Test
  public void testHashCode_shouldHaveSameHashCodesForEqualObjects() {
    Set<DummyMultiCondition> set = Sets.newHashSet();
    set.add(new DummyMultiCondition().add(Conditions.eq(PRICE_CLOSE, 1.0)));
    set.add(new DummyMultiCondition().add(Conditions.eq(PRICE_CLOSE, 1.0)));
    set.add(new DummyMultiCondition().add(Conditions.eq(PRICE_CLOSE, 1.1)));
    set.add(new DummyMultiCondition().add(Conditions.eq(PRICE_CLOSE, 1.0))
        .add(Conditions.gt(PRICE_HIGH, 4)));

    assertThat(set).hasSize(3);
  }

  private static final class DummyMultiCondition extends MultiCondition<DummyMultiCondition> {
    @Override
    DummyMultiCondition getThis() {
      return this;
    }

    @Override
    public boolean isMatch(BarData barParameters) {
      return true;
    }
  }
}
