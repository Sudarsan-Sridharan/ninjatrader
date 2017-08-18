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
public class AtPriceTest {

  private final Price price = Price.builder().open(3).high(5).low(2).close(4).build();
  private final AtPrice atPrice = new AtPrice(5);

  private BarData submittedBarData;
  private BarData currentBarData;

  @Before
  public void before() {
    submittedBarData = mock(BarData.class);
    currentBarData = mock(BarData.class);

    when(currentBarData.getPrice()).thenReturn(price);
  }

  @Test
  public void testIsFulfillable_shouldReturnTrueIfPriceBetweenHighAndLow() {
    assertThat(AtPrice.of(1.999).isFulfillable(submittedBarData, currentBarData)).isFalse();
    assertThat(AtPrice.of(5.00001).isFulfillable(submittedBarData, currentBarData)).isFalse();
    assertThat(AtPrice.of(5).isFulfillable(submittedBarData, currentBarData)).isTrue();
    assertThat(AtPrice.of(2).isFulfillable(submittedBarData, currentBarData)).isTrue();
    assertThat(AtPrice.of(3.333).isFulfillable(submittedBarData, currentBarData)).isTrue();
  }

  @Test
  public void testGetFulfilledPrice_shouldReturnSetPriceIfFulfillable() {
    assertThat(AtPrice.of(5).getFulfilledPrice(submittedBarData, currentBarData)).isEqualTo(5);
    assertThat(AtPrice.of(2).getFulfilledPrice(submittedBarData, currentBarData)).isEqualTo(2);
    assertThat(AtPrice.of(3.00001).getFulfilledPrice(submittedBarData, currentBarData)).isEqualTo(3.00001);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(AtPrice.of(5)).isEqualTo(atPrice);
    assertThat(atPrice).isEqualTo(atPrice).isEqualTo(AtPrice.of(5))
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(AtPrice.of(6));
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(atPrice, AtPrice.of(5), AtPrice.of(6)))
        .containsExactlyInAnyOrder(atPrice, AtPrice.of(6));
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String json = om.writeValueAsString(atPrice);
    assertThat(om.readValue(json, OrderType.class)).isEqualTo(atPrice);
  }
}
