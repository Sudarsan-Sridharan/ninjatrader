//package com.bn.ninjatrader.logical.expression.operation;
//
//import com.bn.ninjatrader.logical.expression.util.TestUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.Sets;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class ArithmeticOperationTest {
//  private static final Logger LOG = LoggerFactory.getLogger(ArithmeticOperationTest.class);
//
//  private final Variable var1 = Variable.of("var1");
//  private final Variable var2 = Variable.of("var2");
//
//  private final ArithmeticOperation orig = ArithmeticOperation.startWith(1).plus(1).minus(var1);
//  private final ArithmeticOperation equal = ArithmeticOperation.startWith(1).plus(1).minus(var1);
//  private final ArithmeticOperation diffLhs = ArithmeticOperation.startWith(2).plus(1).minus(var1);
//  private final ArithmeticOperation diffOperator = ArithmeticOperation.startWith(1).div(1).minus(var1);
//  private final ArithmeticOperation diffVar = ArithmeticOperation.startWith(2).plus(1).minus(var2);
//  private final ArithmeticOperation diffEquation = ArithmeticOperation.startWith(2).plus(1);
//
//
//  @Test
//  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
//    assertThat(equal).isEqualTo(orig);
//    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
//        .isNotEqualTo(null)
//        .isNotEqualTo("")
//        .isNotEqualTo(diffLhs)
//        .isNotEqualTo(diffOperator)
//        .isNotEqualTo(diffVar)
//        .isNotEqualTo(diffEquation);
//  }
//
//  @Test
//  public void testHashCode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
//    assertThat(Sets.newHashSet(orig, equal, diffLhs, diffOperator, diffVar, diffEquation))
//        .containsExactlyInAnyOrder(orig, diffLhs, diffOperator, diffVar, diffEquation);
//  }
//
//  @Test
//  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
//    final ObjectMapper om = TestUtil.om();
//    final String json = om.writeValueAsString(orig);
//    final Operation deserialized = om.readValue(json, Operation.class);
//    assertThat(deserialized).isEqualTo(orig);
//  }
//}
