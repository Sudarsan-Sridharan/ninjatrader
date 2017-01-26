package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.model.DataType;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/18/16.
 */
public class SMADataMapAdaptorTest {

  private static final LocalDate now = LocalDate.of(2016, 2, 1);

  @Test
  public void testToDataMap_shouldSetRSIVariable() {
    final int period = 20;
    final double value = 100.01;
    final SMADataMapAdaptor adaptor = new SMADataMapAdaptor(period);
    final DataMap dataMap = adaptor.toDataMap(Value.of(now, value));
    final Variable variable = Variable.of(DataType.SMA).withPeriod(period);

    assertThat(dataMap).hasSize(1).containsOnlyKeys(variable);
    assertThat(dataMap.get(variable)).isEqualTo(value);
  }
}
