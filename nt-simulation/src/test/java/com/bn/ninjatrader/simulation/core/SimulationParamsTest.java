package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author bradwee2000@gmail.com
 */
public class SimulationParamsTest {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationParamsTest.class);

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final SimulationParams params = new SimulationParams();
    params.setStartingCash(100000);
    params.setSymbol("MEG");

    final String json = om.writeValueAsString(params);
    assertThat(om.readValue(json, SimulationParams.class)).isEqualTo(params);
  }
}
