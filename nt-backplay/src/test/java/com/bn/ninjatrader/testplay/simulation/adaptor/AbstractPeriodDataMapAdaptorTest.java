package com.bn.ninjatrader.testplay.simulation.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.testplay.simulation.data.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/18/16.
 */
public abstract class AbstractPeriodDataMapAdaptorTest {

  private static final Logger log = LoggerFactory.getLogger(AbstractPeriodDataMapAdaptorTest.class);
  private static final int INVALID_PERIOD = 123;

  private LocalDate now;
  private DataType dataType;
  private Value value1;
  private Value value2;

  @BeforeMethod
  public void setup() {
    now = LocalDate.of(2016, 1, 1);
    value1 = Value.of(now, 10);
    value2 = Value.of(now, 100.1234);
    dataType = provideValidDataType();
  }

  @Test
  public void testWithValidValue() {
    DataMapAdaptor adaptor = provideDataMapAdaptorForPeriod(dataType.getPeriod());
    DataMap dataMap = adaptor.toDataMap(value1);
    assertEquals(dataMap.size(), 1);
    assertEquals(dataMap.get(dataType), value1.getValue());

    dataMap = adaptor.toDataMap(value2);
    assertEquals(dataMap.get(dataType), value2.getValue());
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidPeriod() {
    DataMapAdaptor adaptor = provideDataMapAdaptorForPeriod(INVALID_PERIOD);
    adaptor.toDataMap(value1);
  }

  public abstract DataType provideValidDataType();

  public abstract DataMapAdaptor<Value> provideDataMapAdaptorForPeriod(int period);
}
