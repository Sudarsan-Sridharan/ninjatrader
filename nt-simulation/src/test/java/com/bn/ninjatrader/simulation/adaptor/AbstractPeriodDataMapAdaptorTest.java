package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.model.DataType;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractPeriodDataMapAdaptorTest {

  private static final LocalDate now = LocalDate.of(2016, 2, 1);

  @Test
  public void testToDataMap_shouldConvertValueToDataMap() {
    final DummyDataMapAdaptor adaptor = new DummyDataMapAdaptor(20);
    final DataMap dataMap = adaptor.toDataMap(Value.of(now, 1000.01));
    final Variable variable = Variable.of(DataType.PRICE_CLOSE).withPeriod(20);

    assertThat(dataMap).isNotNull().hasSize(1).containsOnlyKeys(variable);
    assertThat(dataMap.get(variable)).isEqualTo(1000.01);
  }

  /**
   * Extend class under test.
   */
  private static class DummyDataMapAdaptor extends AbstractPeriodDataMapAdaptor {

    public DummyDataMapAdaptor(final int period) {
      super(period);
    }

    @Override
    public String getDataType() {
      return DataType.PRICE_CLOSE;
    }
  }
}
