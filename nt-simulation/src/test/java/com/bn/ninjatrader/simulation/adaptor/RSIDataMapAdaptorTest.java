package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.operation.Variable;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class RSIDataMapAdaptorTest {

  private static final LocalDate now = LocalDate.of(2016, 2, 1);

  @Test
  public void testToDataMap_shouldSetRSIVariable() {
    final int period = 14;
    final double value = 400.01;
    final RSIDataMapAdaptor adaptor = new RSIDataMapAdaptor(period);
    final DataMap dataMap = adaptor.toDataMap(Value.of(now, value));
    final Variable variable = Variable.of(DataType.RSI).period(period);

    assertThat(dataMap).hasSize(1).containsOnlyKeys(variable);
    assertThat(dataMap.get(variable)).isEqualTo(value);
  }
}
