package com.bn.ninjatrader.model.datastore.entity;

import com.bn.ninjatrader.model.datastore.util.DummyObjectMapperProvider;
import com.bn.ninjatrader.model.entity.Price;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceDatastoreTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = PriceDatastore.builder()
      .date(now).open(5).high(10).low(4).close(6).volume(10000).build();

  @Test
  public void testBuilder_shouldSetProperties() {
    assertThat(price.getDate()).isEqualTo(now);
    assertThat(price.getOpen()).isEqualTo(5.0);
    assertThat(price.getHigh()).isEqualTo(10.0);
    assertThat(price.getLow()).isEqualTo(4.0);
    assertThat(price.getClose()).isEqualTo(6.0);
    assertThat(price.getChange()).isEqualTo(0.0);
    assertThat(price.getVolume()).isEqualTo(10000);
  }

  @Test
  public void testCopy_shouldReturnEqualObject() {
    assertThat(PriceDatastore.builder().copyOf(price).build()).isEqualTo(price);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnEqualObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String json = om.writeValueAsString(price);
    assertThat(om.readValue(json, Price.class)).isEqualTo(price);
  }
}
