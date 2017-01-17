package com.bn.ninjatrader.simulation.order;

import com.beust.jcommander.internal.Sets;
import com.bn.ninjatrader.common.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/29/16.
 */
public class SellOrderParametersTest {

  private final SellOrderParameters orig = OrderParameters.sell().at(MarketTime.OPEN).barsFromNow(100).build();
  private final SellOrderParameters same = OrderParameters.sell().at(MarketTime.OPEN).barsFromNow(100).build();
  private final SellOrderParameters diff1 = OrderParameters.sell().at(MarketTime.OPEN).barsFromNow(101).build();
  private final SellOrderParameters diff2 = OrderParameters.sell().at(MarketTime.CLOSE).barsFromNow(100).build();
  private final ObjectMapper om = TestUtil.objectMapper();

  @Test
  public void testCreateWithBuilder_shouldSetValuesCorrectly() {
    SellOrderParameters params = OrderParameters.sell().at(MarketTime.OPEN).barsFromNow(100).build();
    assertThat(params.getBarsFromNow()).isEqualTo(100);
    assertThat(params.getMarketTime()).isEqualTo(MarketTime.OPEN);
  }

  @Test
  public void testEquals_shouldBeEqualsIfObjectsHaveEqualPropValues() {
    assertThat(orig).isEqualTo(orig).isEqualTo(same);
    assertThat(same).isEqualTo(orig);
    assertThat(orig).isNotEqualTo(diff1).isNotEqualTo(diff2);
  }

  @Test
  public void testHashCode_shouldHaveSameHashCodeIfObjectsHaveEqualPropValues() {
    Set<SellOrderParameters> set = Sets.newHashSet();
    set.add(orig);
    set.add(same);
    set.add(diff1);
    set.add(diff2);
    assertThat(set).containsOnly(orig, diff1, diff2);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    String serialized = om.writeValueAsString(orig);
    OrderParameters deserialized = om.readValue(serialized, OrderParameters.class);
    assertThat(deserialized).isInstanceOf(SellOrderParameters.class).isEqualTo(orig);
  }
}
