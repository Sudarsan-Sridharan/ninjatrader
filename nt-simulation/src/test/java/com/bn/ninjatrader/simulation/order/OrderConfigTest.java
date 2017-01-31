package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.common.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author bradwee2000@gmail.com
 */
public class OrderConfigTest {

  private final OrderConfig orig = OrderConfig.withBarsFromNow(10).expireAfterNumOfBars(20);
  private final OrderConfig equal = OrderConfig.withExpireAfterNumOfBars(20).barsFromNow(10);
  private final OrderConfig diffBarsFromNow = OrderConfig.withBarsFromNow(5).expireAfterNumOfBars(20);
  private final OrderConfig diffExpire = OrderConfig.withBarsFromNow(20).expireAfterNumOfBars(200);

  @Test
  public void testDefault_shouldSetDefaultConfig() {
    final OrderConfig defaultConfig = OrderConfig.defaults();
    assertThat(defaultConfig.getBarsFromNow()).isEqualTo(0);
    assertThat(defaultConfig.getExpireAfterNumOfBars()).isEqualTo(Integer.MAX_VALUE);
  }

  @Test
  public void testCreate_shouldSetProperties() {
    assertThat(orig.getBarsFromNow()).isEqualTo(10);
    assertThat(orig.getExpireAfterNumOfBars()).isEqualTo(20);
  }

  @Test
  public void testWithExpireLessThanBarsFromNow_shouldThrowException() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      OrderConfig.withBarsFromNow(10).expireAfterNumOfBars(9);
    });
  }

  @Test
  public void testEqual_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(equal).isEqualTo(orig)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffBarsFromNow)
        .isNotEqualTo(diffExpire);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeifAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffBarsFromNow, diffExpire))
        .containsExactlyInAnyOrder(orig, diffBarsFromNow, diffExpire);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, OrderConfig.class)).isEqualTo(orig);
  }
}
