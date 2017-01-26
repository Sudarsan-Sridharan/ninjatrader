package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Brad on 8/5/16.
 */
public class MultiConditionTest {
  private static final Logger LOG = LoggerFactory.getLogger(MultiConditionTest.class);

  final Variable variable1 = Variable.of("DataType1");
  final Variable variable2 = Variable.of("DataType2");
  final Variable variable3 = Variable.of("DataType3");

  @Test
  public void testCreate_shouldHaveZeroConditions() {
    final DummyMultiCondition condition = DummyMultiCondition.newInstance();
    assertThat(condition.getConditions()).isNotNull().hasSize(0);
  }

  @Test
  public void testAddCondition_shouldAddConditionToList() {
    final DummyMultiCondition condition = DummyMultiCondition.newInstance();
    condition.add(Conditions.eq(variable1, 12));
    condition.add(Conditions.eq(variable1, 12));
    assertThat(condition.getConditions()).hasSize(2);
  }

  @Test
  public void testGetVariables_shouldReturnAllVariablesInAllConditions() {
    final DummyMultiCondition condition = DummyMultiCondition.newInstance();
    condition.add(Conditions.eq(variable1, 12));
    condition.add(Conditions.eq(variable2, variable3));
    assertThat(condition.getVariables()).hasSize(3).containsOnly(variable1, variable2, variable3);
  }

  @Test
  public void testEqualsWithSameConditions_shouldReturnEqual() {
    final DummyMultiCondition condition = DummyMultiCondition.newInstance()
        .add(Conditions.eq(variable1, 1.0))
        .add(Conditions.eq(variable2, variable3));
    final DummyMultiCondition equalCondition = DummyMultiCondition.newInstance()
        .add(Conditions.eq(variable1, 1.0))
        .add(Conditions.eq(variable2, variable3));

    assertThat(condition).isEqualTo(equalCondition);
    assertThat(equalCondition).isEqualTo(condition);
  }

  @Test
  public void testEqualsWithDiffConditions_shouldReturnNotEqual() {
    final DummyMultiCondition orig = DummyMultiCondition.newInstance().add(Conditions.eq(variable1, 1.0));
    final DummyMultiCondition diff1 = DummyMultiCondition.newInstance().add(Conditions.eq(variable1, 1.1));
    final DummyMultiCondition diff2 = DummyMultiCondition.newInstance().add(Conditions.eq(variable1, 1.0))
        .add(Conditions.gt(variable2, 4));

    assertThat(orig).isNotEqualTo(diff1);
    assertThat(orig).isNotEqualTo(diff2);
  }

  @Test
  public void testHashCode_shouldHaveSameHashCodesForEqualObjects() {
    Set<DummyMultiCondition> set = Sets.newHashSet();
    set.add(DummyMultiCondition.newInstance().add(Conditions.eq(variable1, 1.0)));
    set.add(DummyMultiCondition.newInstance().add(Conditions.eq(variable1, 1.0)));
    set.add(DummyMultiCondition.newInstance().add(Conditions.eq(variable1, 1.1)));
    set.add(DummyMultiCondition.newInstance().add(Conditions.eq(variable1, 1.0))
        .add(Conditions.gt(variable2, 4)));

    assertThat(set).hasSize(3);
  }

  private static final class DummyMultiCondition<T extends Data> extends MultiCondition<DummyMultiCondition<T>, T> {
    public static final <T extends Data> DummyMultiCondition<T> newInstance() {
      return new DummyMultiCondition<>();
    }

    @Override
    DummyMultiCondition<T> getThis() {
      return this;
    }

    @Override
    public boolean isMatch(Data data) {
      return false;
    }
  }
}
