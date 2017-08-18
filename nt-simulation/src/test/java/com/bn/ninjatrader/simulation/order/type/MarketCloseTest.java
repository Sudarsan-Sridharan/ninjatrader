package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
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

  private BarData submittedBarData;
  private BarData currentBarData;

  @Before
  public void before() {
    submittedBarData = mock(BarData.class);
    currentBarData = mock(BarData.class);

    when(currentBarData.getPrice()).thenReturn(price);
  }

  @Test
  public void testIsFulfillable_shouldAlwaysReturnTrue() {
    assertThat(marketClose.isFulfillable(submittedBarData, currentBarData)).isTrue();
  }

  @Test
  public void testGetFulfilledPrice_shouldReturnPriceAtClose() {
    assertThat(marketClose.getFulfilledPrice(submittedBarData, currentBarData)).isEqualTo(4.4);
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
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String json = om.writeValueAsString(marketClose);
    assertThat(om.readValue(json, OrderType.class)).isEqualTo(marketClose);
  }
}
