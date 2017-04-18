package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.simulation.script.AlgorithmScript;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author bradwee2000@gmail.com
 */
public class SimulationRequestTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final LocalDate tomorrow = LocalDate.of(2016, 2, 2);
  private final AlgorithmScript script = mock(AlgorithmScript.class);
  private final SimulationRequest req = SimulationRequest.withSymbol("MEG")
      .from(now).to(tomorrow).algorithmScript(script);

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(req.getSymbol()).isEqualTo("MEG");
    assertThat(req.getFrom()).isEqualTo(now);
    assertThat(req.getTo()).isEqualTo(tomorrow);
    assertThat(req.getAlgorithmScript()).isEqualTo(script);
  }
}
