package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class ScanResultTest {

  private final ScanResult scanResult = ScanResult.builder()
      .symbol("MEG")
      .profit(10000)
      .profitPcnt(0.65)
      .lastTransaction(BuyTransaction.buy().build())
      .build();

  @Test
  public void testCreate_shouldSetProperties() {
    assertThat(scanResult)
        .extracting("symbol", "profit", "profitPcnt", "lastTransaction")
        .containsExactly("MEG", 10000.0, 0.65, BuyTransaction.buy().build());
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String json = om.writeValueAsString(scanResult);
    assertThat(om.readValue(json, ScanResult.class)).isEqualTo(scanResult);
  }
}
