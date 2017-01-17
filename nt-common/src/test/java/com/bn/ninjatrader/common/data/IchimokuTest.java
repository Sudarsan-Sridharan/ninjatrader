package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author bradwee2000@gmail.com
 */
public class IchimokuTest {
  private final LocalDate now = LocalDate.of(2016, 2, 1);

  private final Ichimoku orig = Ichimoku.builder().date(now)
      .tenkan(1.1).kijun(2.1).chikou(3.1).senkouA(4.1).senkouB(5.1).build();
  private final Ichimoku equal = Ichimoku.builder().date(now)
      .tenkan(1.1).kijun(2.1).chikou(3.1).senkouA(4.1).senkouB(5.1).build();

  private final Ichimoku diffDate = Ichimoku.builder().date(now.plusDays(1))
      .tenkan(1.1).kijun(2.1).chikou(3.1).senkouA(4.1).senkouB(5.1).build();
  private final Ichimoku diffTenkan = Ichimoku.builder().date(now)
      .tenkan(1.2).kijun(2.1).chikou(3.1).senkouA(4.1).senkouB(5.1).build();
  private final Ichimoku diffKijun = Ichimoku.builder().date(now)
      .tenkan(1.1).kijun(2.2).chikou(3.1).senkouA(4.1).senkouB(5.1).build();
  private final Ichimoku diffChikou = Ichimoku.builder().date(now)
      .tenkan(1.1).kijun(2.1).chikou(3.2).senkouA(4.1).senkouB(5.1).build();
  private final Ichimoku diffSenkouA = Ichimoku.builder().date(now)
      .tenkan(1.1).kijun(2.1).chikou(3.1).senkouA(4.2).senkouB(5.1).build();
  private final Ichimoku diffSenkouB = Ichimoku.builder().date(now)
      .tenkan(1.1).kijun(2.1).chikou(3.1).senkouA(4.1).senkouB(5.2).build();

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getDate()).isEqualTo(now);
    assertThat(orig.getTenkan()).isEqualTo(1.1);
    assertThat(orig.getKijun()).isEqualTo(2.1);
    assertThat(orig.getChikou()).isEqualTo(3.1);
    assertThat(orig.getSenkouA()).isEqualTo(4.1);
    assertThat(orig.getSenkouB()).isEqualTo(5.1);
  }

  @Test
  public void testCopy_shouldReturnEqualObject() {
    assertThat(Ichimoku.builder().copyOf(orig).build()).isEqualTo(orig);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(diffDate).isNotEqualTo(diffChikou).isNotEqualTo(diffTenkan)
        .isNotEqualTo(diffKijun).isNotEqualTo(diffSenkouA).isNotEqualTo(diffSenkouB);
  }

  @Test
  public void testHashcode_shouldHaveSameHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffDate, diffChikou, diffTenkan, diffKijun, diffSenkouA, diffSenkouB))
        .containsExactlyInAnyOrder(orig, diffDate, diffChikou, diffTenkan, diffKijun, diffSenkouA, diffSenkouB);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, Ichimoku.class)).isEqualTo(orig);
  }
}
