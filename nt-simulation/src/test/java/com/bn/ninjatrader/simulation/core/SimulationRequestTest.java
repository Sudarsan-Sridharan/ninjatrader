package com.bn.ninjatrader.simulation.core;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class SimulationRequestTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final LocalDate tomorrow = LocalDate.of(2016, 2, 2);
  private final SimulationRequest req = SimulationRequest.withSymbol("MEG")
      .from(now).to(tomorrow).tradeAlgorithmId("algoId");

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(req.getSymbol()).isEqualTo("MEG");
    assertThat(req.getFrom()).isEqualTo(now);
    assertThat(req.getTo()).isEqualTo(tomorrow);
    assertThat(req.getTradeAlgorithmId()).isEqualTo("algoId");
  }
}
