package com.bn.ninjatrader.testplay.simulation.data;

import com.beust.jcommander.internal.Maps;
import com.bn.ninjatrader.common.data.DataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Created by Brad on 8/18/16.
 */
public class DataMapTest {

  private DataMap dataMap;

  @BeforeMethod
  public void setup() {
    dataMap = new DataMap();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testGetWithEmptyMap() {
    assertEquals(dataMap.size(), 0);
    assertNull(dataMap.get(DataType.PRICE_OPEN));
  }

  @Test
  public void testPutKeyValue() {
    dataMap.put(DataType.PRICE_OPEN, 10);
    dataMap.put(DataType.PRICE_CLOSE, 20);

    assertEquals(dataMap.size(), 2);
    assertEquals(dataMap.get(DataType.PRICE_OPEN), 10d);
    assertEquals(dataMap.get(DataType.PRICE_CLOSE), 20d);
  }

  @Test
  public void testPutHashMap() {
    Map<DataType, Double> map = Maps.newHashMap();
    map.put(DataType.PRICE_OPEN, 10d);
    map.put(DataType.PRICE_CLOSE, 20d);

    dataMap.put(map);
    assertEquals(dataMap.size(), 2);
    assertEquals(dataMap.get(DataType.PRICE_OPEN), 10d);
    assertEquals(dataMap.get(DataType.PRICE_CLOSE), 20d);
  }

  @Test
  public void testPutDataMap() {
    DataMap subDataMap = new DataMap();
    subDataMap.put(DataType.PRICE_OPEN, 10d);
    subDataMap.put(DataType.PRICE_CLOSE, 20d);

    dataMap.put(subDataMap);
    assertEquals(dataMap.size(), 2);
    assertEquals(dataMap.get(DataType.PRICE_OPEN), 10d);
    assertEquals(dataMap.get(DataType.PRICE_CLOSE), 20d);
  }

  @Test
  public void testClearDataMap() {
    dataMap.put(DataType.PRICE_OPEN, 10);
    dataMap.put(DataType.PRICE_CLOSE, 20);

    dataMap.clear();

    assertEquals(dataMap.size(), 0);
  }
}
