//package com.bn.ninjatrader.logical.expression.operation;
//
//import com.bn.ninjatrader.logical.expression.model.Data;
//import com.bn.ninjatrader.logical.expression.util.TestUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.Sets;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//
//import static com.bn.ninjatrader.logical.expression.operator.Operator.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * Created by Brad on 8/2/16.
// */
//public class BinaryOperationTest {
//  private static final Logger LOG = LoggerFactory.getLogger(BinaryOperationTest.class);
//
//  private final Variable var1 = Variable.of("DataType1");
//  private final Variable var2 = Variable.of("DataType2");
//  private final Variable var3 = Variable.of("DataType3");
//  private final Variable var4 = Variable.of("DataType4");
//  private final Variable var5 = Variable.of("DataType5");
//
//  private final BinaryOperation orig = BinaryOperation.of(var1, PLUS, var2);
//  private final BinaryOperation equal = BinaryOperation.of(var1, PLUS, var2);
//  private final BinaryOperation diffLhs = BinaryOperation.of(var3, PLUS, var2);
//  private final BinaryOperation diffRhs = BinaryOperation.of(var1, PLUS, var3);
//  private final BinaryOperation diffOperator = BinaryOperation.of(var1, MINUS, var2);
//
//
//  private final Data data = new Data() {
//    @Override
//    public Double get(final Variable variable) {
//      if (variable == var1) {
//        return 1.0;
//      } else if (variable == var2) {
//        return 2.0;
//      } else if (variable == var3) {
//        return 3.0;
//      } else if (variable == var4) {
//        return 4.0;
//      } else if (variable == var5) {
//        return 10000d;
//      }
//      return 0d;
//    }
//  };
//
//  @Test
//  public void testGetValueOnEquationWithOnlyConstants_shouldReturnCalculatedValue() {
//    assertThat(BinaryOperation.of(5.6, PLUS, 5.8).getValue(data)).isEqualTo(11.4);
//    assertThat(BinaryOperation.of(-1.1, PLUS, 1.1).getValue(data)).isEqualTo(0);
//    assertThat(BinaryOperation.of(0, PLUS, -0).getValue(data)).isEqualTo(0);
//
//    assertThat(BinaryOperation.of(5.1, MINUS, 4.1).getValue(data)).isEqualTo(1.0);
//    assertThat(BinaryOperation.of(0, MINUS, 0).getValue(data)).isEqualTo(0);
//    assertThat(BinaryOperation.of(1, MINUS, 2).getValue(data)).isEqualTo(-1);
//    assertThat(BinaryOperation.of(1, MINUS, -1).getValue(data)).isEqualTo(2);
//  }
//
//  @Test
//  public void testGetValueOnEquationWithVariables_shouldReturnCalculatedValue() {
//    assertThat(BinaryOperation.of(var4, PLUS, var2).getValue(data)).isEqualTo(6.0);
//    assertThat(BinaryOperation.of(var4, PLUS, var4).getValue(data)).isEqualTo(8.0);
//  }
//
//  @Test
//  public void testGetVariablesOnConstantOnlyEquation_shouldReturnEmpty() {
//    assertThat(BinaryOperation.of(1.0, PLUS, 2.0).getVariables()).isEmpty();
//  }
//
//  @Test
//  public void testGetVariables_shouldReturnAllVariablesInEquation() {
//    assertThat(BinaryOperation.of(var1, PLUS, var1).getVariables())
//        .containsExactlyInAnyOrder(var1);
//
//    assertThat(BinaryOperation.of(var1, PLUS, var4).getVariables())
//        .containsExactlyInAnyOrder(var1, var4);
//  }
//
//  @Test
//  public void testGetVariablesOfNestedOperations_shouldReturnAllVariablesInEquation() {
//    BinaryOperation operation = BinaryOperation.of(var1, PLUS, var4);
//    operation = BinaryOperation.of(operation, MINUS, 1.0);
//    operation = BinaryOperation.of(operation, MULTIPLY, var2);
//
//    assertThat(operation.getVariables()).containsExactlyInAnyOrder(var1, var4, var2);
//  }
//
//  @Test
//  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
//    assertThat(equal).isEqualTo(orig);
//    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
//        .isNotEqualTo(null)
//        .isNotEqualTo("")
//        .isNotEqualTo(diffLhs)
//        .isNotEqualTo(diffRhs)
//        .isNotEqualTo(diffOperator);
//  }
//
//  @Test
//  public void testHashCode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
//    assertThat(Sets.newHashSet(orig, equal, diffLhs, diffRhs, diffOperator))
//        .containsExactlyInAnyOrder(orig, diffLhs, diffRhs, diffOperator);
//  }
//
//  @Test
//  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
//    final ObjectMapper om = TestUtil.om();
//    final BinaryOperation operation = BinaryOperation.of(var1, PLUS, BinaryOperation.of(3, PLUS, var2));
//    final String json = om.writeValueAsString(operation);
//    final Operation deserialized = om.readValue(json, Operation.class);
//    assertThat(deserialized).isEqualTo(operation);
//  }
//
//  @Test
//  public void testToString_shouldConvertVariablesToConstant() {
//    final Data data = mock(Data.class);
//    when(data.get(var1)).thenReturn(20d);
//    when(data.get(var2)).thenReturn(100.03);
//
//    assertThat(orig.toString(data)).isEqualTo("(20.0 + 100.03)");
//  }
//}
