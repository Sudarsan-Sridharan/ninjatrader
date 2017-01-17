package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Brad on 7/11/16.
 */
public class PriceTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final LocalDate tomorrow = now.plusDays(1);

  private final Price orig = Price.builder().date(now).open(5).high(10).low(4).close(6).volume(10000).build();
  private final Price equal = Price.builder().date(now).open(5).high(10).low(4).close(6).volume(10000).build();
  private final Price diffDate = Price.builder().date(tomorrow).open(5).high(10).low(4).close(6).volume(10000).build();
  private final Price diffOpen = Price.builder().date(now).open(5.1).high(10).low(4).close(6).volume(10000).build();
  private final Price diffHigh = Price.builder().date(now).open(5).high(10.1).low(4).close(6).volume(10000).build();
  private final Price diffLow = Price.builder().date(now).open(5).high(10).low(4.1).close(6).volume(10000).build();
  private final Price diffClose = Price.builder().date(now).open(5).high(10).low(4).close(6.1).volume(10000).build();
  private final Price diffChange = Price.builder()
      .date(now).open(5).high(10).low(4).close(6).volume(10000).change(1.1).build();
  private final Price diffVolume = Price.builder().date(now).open(5).high(10).low(4).close(6).volume(10001).build();

  @Test
  public void testBuilder_shouldSetProperties() {
    assertThat(orig.getDate()).isEqualTo(now);
    assertThat(orig.getOpen()).isEqualTo(5.0);
    assertThat(orig.getHigh()).isEqualTo(10.0);
    assertThat(orig.getLow()).isEqualTo(4.0);
    assertThat(orig.getClose()).isEqualTo(6.0);
    assertThat(orig.getChange()).isEqualTo(0.0);
    assertThat(orig.getVolume()).isEqualTo(10000);
  }

  @Test
  public void testCopy_shouldReturnEqualObject() {
    assertThat(Price.builder().copyOf(orig).build()).isEqualTo(orig);
  }

  @Test
  public void testHashCode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffOpen, diffHigh, diffLow, diffClose, diffVolume, diffChange, diffDate))
        .containsExactlyInAnyOrder(orig, diffOpen, diffHigh, diffLow, diffClose, diffChange, diffVolume, diffDate);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffOpen)
        .isNotEqualTo(diffHigh)
        .isNotEqualTo(diffLow)
        .isNotEqualTo(diffClose)
        .isNotEqualTo(diffChange)
        .isNotEqualTo(diffVolume)
        .isNotEqualTo(diffDate);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, Price.class)).isEqualTo(orig);
  }
}
