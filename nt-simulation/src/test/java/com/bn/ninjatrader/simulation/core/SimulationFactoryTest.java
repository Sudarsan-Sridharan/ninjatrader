package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.operation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Set;

import static com.bn.ninjatrader.simulation.condition.Conditions.create;
import static com.bn.ninjatrader.simulation.condition.Conditions.eq;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_HIGH;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author bradwee2000@gmail.com
 */
public class SimulationFactoryTest {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationFactoryTest.class);

  @Test
  public void testGetVariables_shouldReturnAllVariables() {
    SimulationParams params = new SimulationParams();
    params.setBuyCondition(create().add(eq(PRICE_CLOSE, 5.0)));
    params.setSellCondition(create().add(eq(PRICE_HIGH, 7.0)));

    Set<Variable> variables = params.getVariables();

    assertThat(variables).hasSize(2);
    assertThat(variables).contains(PRICE_CLOSE, PRICE_HIGH);
  }

  @Test
  public void testGetDataTypes_shouldReturnAllDataTypes() {
    SimulationParams params = new SimulationParams();
    params.setBuyCondition(create().add(eq(PRICE_CLOSE, 5.0)));
    params.setSellCondition(create().add(eq(PRICE_HIGH, 7.0)));

    Set<DataType> dataTypes = params.getDataTypes();

    assertThat(dataTypes).hasSize(2);
    assertThat(dataTypes).contains(DataType.PRICE_CLOSE, DataType.PRICE_HIGH);
  }
}
