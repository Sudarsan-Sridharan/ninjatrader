package com.bn.ninjatrader.testplay.simulation.data.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.common.data.DataType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Created by Brad on 8/18/16.
 */
public class SimpleAverageDataMapAdaptorTest {

  private LocalDate now;

  @BeforeClass
  public void setup() {
    now = LocalDate.of(2016, 1, 1);
  }

  @Test
  public void testToDataMap() {
    SimpleAverageDataMapAdaptor adaptor = SimpleAverageDataMapAdaptor.forPeriod(10);
    Value value = Value.of(now, 10);
    DataMap dataMap = adaptor.toDataMap(value);

    assertEquals(dataMap.get(DataType.SMA_10), value.getValue());
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNonExistingDataType() {
    SimpleAverageDataMapAdaptor adaptor = SimpleAverageDataMapAdaptor.forPeriod(20);
    Value value = Value.of(now, 10);
    DataMap dataMap = adaptor.toDataMap(value);

    assertNull(dataMap.get(DataType.SMA_21));
  }
}
