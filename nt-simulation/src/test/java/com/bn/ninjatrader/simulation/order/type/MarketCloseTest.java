package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class MarketCloseTest {

  private final Price price = Price.builder().open(1).high(2).low(3).close(4.4).build();
  private final MarketClose marketClose = new MarketClose();

  private BarData barData;

  @Before
  public void before() {
    barData = mock(BarData.class);

    when(barData.getPrice()).thenReturn(price);
  }

  @Test
  public void testIsFulfillable_shouldAlwaysReturnTrue() {
    assertThat(marketClose.isFulfillable(barData)).isTrue();
  }

  @Test
  public void testGetFulfilledPrice_shouldReturnPriceAtClose() {
    assertThat(marketClose.getFulfilledPrice(barData)).isEqualTo(4.4);
  }

  @Test
  public void testGetVariables_shouldReturnEmptySet() {
    assertThat(marketClose.getVariables()).isEmpty();
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(marketClose).isEqualTo(new MarketClose())
        .isNotEqualTo(null)
        .isNotEqualTo("");
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(marketClose, new MarketClose()))
        .containsExactlyInAnyOrder(marketClose);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(marketClose);
    assertThat(om.readValue(json, OrderType.class)).isEqualTo(marketClose);
  }
}
