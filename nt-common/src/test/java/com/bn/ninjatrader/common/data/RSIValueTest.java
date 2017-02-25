//package com.bn.ninjatrader.common.data;
//
//import com.bn.ninjatrader.model.util.TestUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.Sets;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class RSIValueTest {
//
//  private final LocalDate now = LocalDate.of(2016, 2, 1);
//  private final RSIValue orig = RSIValue.of(now, 1.1, 1.3, 1.01);
//  private final RSIValue equal = RSIValue.of(now, 1.1, 1.3, 1.01);
//  private final RSIValue diffDate = RSIValue.of(now.plusDays(1), 1.1, 1.3, 1.01);
//  private final RSIValue diffValue = RSIValue.of(now, 9.9, 1.3, 1.01);
//  private final RSIValue diffAvgGain = RSIValue.of(now, 1.1, 9.3, 1.01);
//  private final RSIValue diffAvgLoss = RSIValue.of(now, 1.1, 1.3, 9.01);
//
//  @Test
//  public void testCreate_shouldSetProperties() {
//    assertThat(orig.getDate()).isEqualTo(now);
//    assertThat(orig.getValue()).isEqualTo(1.1);
//    assertThat(orig.getAvgGain()).isEqualTo(1.3);
//    assertThat(orig.getAvgLoss()).isEqualTo(1.01);
//  }
//
//  @Test
//  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
//    assertThat(equal).isEqualTo(orig);
//    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
//        .isNotEqualTo(null)
//        .isNotEqualTo("")
//        .isNotEqualTo(diffDate)
//        .isNotEqualTo(diffValue)
//        .isNotEqualTo(diffAvgGain)
//        .isNotEqualTo(diffAvgLoss);
//  }
//
//  @Test
//  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
//    assertThat(Sets.newHashSet(orig, equal, diffDate, diffValue, diffAvgGain, diffAvgLoss))
//        .containsExactlyInAnyOrder(orig, diffDate, diffValue, diffAvgGain, diffAvgLoss);
//  }
//
//  @Test
//  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
//    final ObjectMapper om = TestUtil.objectMapper();
//    final String json = om.writeValueAsString(orig);
//    final RSIValue deserialized = om.readValue(json, RSIValue.class);
//    assertThat(deserialized).isEqualTo(orig);
//  }
//}
