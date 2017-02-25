package com.bn.ninjatrader.thirdparty.psetrader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class PseTraderQuoteTest {
  private static final String DATA =
      "[{\"d\":\"20170222\",\"s\":\"PNB\",\"o\":60.0,\"h\":60.0,\"l\":59.35,\"c\":59.35,\"v\":7350}," +
      "{\"d\":\"20170222\",\"s\":\"ACR\",\"o\":1.4,\"h\":1.43,\"l\":1.4,\"c\":1.42,\"v\":316000}]";

  @Test
  public void testDeserialize_shouldConvertJsonToObject() throws IOException {
    final ObjectMapper om = new ObjectMapper();
    List<PseTraderQuote> quotes = om.readValue(DATA, new TypeReference<List<PseTraderQuote>>() {});
    assertThat(quotes.get(0).getDailyQuote())
        .extracting("date", "symbol", "open", "high", "low", "close", "volume")
        .containsExactly(LocalDate.of(2017,2, 22), "PNB", 60.0, 60.0, 59.35, 59.35, 7350l);
    assertThat(quotes.get(1).getDailyQuote())
        .extracting("date", "symbol", "open", "high", "low", "close", "volume")
        .containsExactly(LocalDate.of(2017,2, 22), "ACR", 1.4, 1.43, 1.4, 1.42, 316000l);
  }
}
