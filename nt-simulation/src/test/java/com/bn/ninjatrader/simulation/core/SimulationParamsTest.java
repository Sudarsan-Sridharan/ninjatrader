package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.logical.expression.condition.Conditions;
import com.bn.ninjatrader.simulation.model.DataType;
import com.bn.ninjatrader.simulation.operation.Variables;
import com.bn.ninjatrader.simulation.statement.ConditionalStatement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_HIGH;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author bradwee2000@gmail.com
 */
public class SimulationParamsTest {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationParamsTest.class);

  @Test
  public void testGetVariables_shouldReturnAllVariables() {
    final SimulationParams params = SimulationParams.builder()
        .addStatement(ConditionalStatement.builder().condition(Conditions.eq(Variables.PRICE_CLOSE, 1.0)).build())
        .addStatement(ConditionalStatement.builder().condition(Conditions.lt(Variables.PRICE_HIGH, 100)).build())
        .build();

    assertThat(params.getVariables()).containsExactlyInAnyOrder(PRICE_CLOSE, PRICE_HIGH);
  }

  @Test
  public void testGetDataTypes_shouldReturnAllDataTypes() {
    final SimulationParams params = SimulationParams.builder()
        .addStatement(ConditionalStatement.builder().condition(Conditions.eq(Variables.PRICE_CLOSE, 1.0)).build())
        .addStatement(ConditionalStatement.builder().condition(Conditions.lt(Variables.PRICE_HIGH, 100)).build())
        .build();

    assertThat(params.getDataTypes()).contains(DataType.PRICE_CLOSE, DataType.PRICE_HIGH);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final SimulationParams params = new SimulationParams();
    params.setStartingCash(100000);
    params.setSymbol("MEG");

    final String json = om.writeValueAsString(params);
    assertThat(om.readValue(json, SimulationParams.class)).isEqualTo(params);
  }
}
