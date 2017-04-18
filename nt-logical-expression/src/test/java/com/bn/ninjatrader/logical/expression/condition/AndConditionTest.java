//package com.bn.ninjatrader.logical.expression.condition;
//
//
//import com.bn.ninjatrader.logical.expression.model.Data;
//import com.bn.ninjatrader.logical.expression.operation.Variable;
//import com.bn.ninjatrader.logical.expression.util.TestUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * Created by Brad on 8/5/16.
// */
//public class AndConditionTest {
//  private static final Logger LOG = LoggerFactory.getLogger(AndConditionTest.class);
//
//  private final Condition trueCondition = new TrueCondition();
//  private final Condition falseCondition = new FalseCondition();
//  private final Data barData = new MockData();
//
//  @Test
//  public void testNullCondition_shouldReturnTrue() {
//    assertThat(new AndCondition().isMatch(barData)).isTrue();
//  }
//
//  @Test
//  public void testAllTrue_shouldReturnTrue() {
//    assertThat(new AndCondition(trueCondition, trueCondition).isMatch(barData)).isTrue();
//  }
//
//  @Test
//  public void testAllFalse_shouldReturnFalse() {
//    assertThat(new AndCondition(falseCondition, falseCondition).isMatch(barData)).isFalse();
//  }
//
//  @Test
//  public void testMixedTrueAndFalse_shouldReturnFalse() {
//    assertThat(new AndCondition(trueCondition, trueCondition, trueCondition, falseCondition).isMatch(barData))
//        .isFalse();
//
//    assertThat(new AndCondition(falseCondition, trueCondition, trueCondition).isMatch(barData))
//        .isFalse();
//  }
//
//  @Test
//  public void testAddCondition_shouldReturnFalseIfOneIsFalse() {
//    assertThat(new AndCondition(trueCondition, trueCondition).isMatch(barData)).isTrue();
//    assertThat(new AndCondition(trueCondition, trueCondition, falseCondition).isMatch(barData)).isFalse();
//  }
//
//  @Test
//  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
//    final ObjectMapper om = TestUtil.om();
//    final AndCondition condition = new AndCondition<>().add(Conditions.eq(Variable.of("DataType1"), 1.0));
//    final String serialized = om.writeValueAsString(condition);
//    final Condition deserialized = om.readValue(serialized, Condition.class);
//    assertThat(deserialized).isInstanceOf(AndCondition.class).isEqualTo(condition);
//  }
//}
