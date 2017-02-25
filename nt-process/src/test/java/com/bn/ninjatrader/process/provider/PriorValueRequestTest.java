//package com.bn.ninjatrader.process.provider;
//
//import com.beust.jcommander.internal.Lists;
//import com.bn.ninjatrader.common.type.TimeFrame;
//import org.junit.Test;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class PriorValueRequestTest {
//
//  private final LocalDate now = LocalDate.of(2016, 2, 1);
//
//  private PriorValueRequest orig = PriorValueRequest.builder()
//      .symbol("MEG").priorDate(now).timeFrame(TimeFrame.ONE_DAY).addPeriod(10).addPeriods(20, 50).build();
//  private PriorValueRequest equal = PriorValueRequest.builder()
//      .symbol("MEG").priorDate(now).timeFrame(TimeFrame.ONE_DAY).addPeriods(Lists.newArrayList(10, 20, 50)).build();
//
//  @Test
//  public void testBuild_shouldSetParameters() {
//    assertThat(orig.getSymbol()).isEqualTo("MEG");
//    assertThat(orig.getTimeFrame()).isEqualTo(TimeFrame.ONE_DAY);
//    assertThat(orig.getPriorDate()).isEqualTo(now);
//    assertThat(orig.getPeriods()).containsExactly(10, 20, 50);
//  }
//
//  @Test
//  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
//    assertThat(equal).isEqualTo(orig);
//    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
//        .isNotEqualTo(null)
//        .isNotEqualTo("");
//  }
//}
