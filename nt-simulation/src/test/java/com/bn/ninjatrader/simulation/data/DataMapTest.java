package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.simulation.logic.Variable;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.bn.ninjatrader.simulation.logic.Variables.PRICE_CLOSE;
import static com.bn.ninjatrader.simulation.logic.Variables.PRICE_OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Created by Brad on 8/18/16.
 */
public class DataMapTest {

  private DataMap dataMap;

  @Before
  public void setup() {
    dataMap = new DataMap();
  }

  @Test
  public void testGetWithEmptyMap_shouldThrowException() {
    assertThat(dataMap.size()).isEqualTo(0);
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> dataMap.get(PRICE_OPEN));
  }

  @Test
  public void testPutKeyValue_shouldAssignValuesToVariables() {
    dataMap.put(PRICE_OPEN, 10);
    dataMap.put(PRICE_CLOSE, 20);

    assertThat(dataMap.size()).isEqualTo(2);
    assertThat(dataMap.get(PRICE_OPEN)).isEqualTo(10d);
    assertThat(dataMap.get(PRICE_CLOSE)).isEqualTo(20d);
  }

  @Test
  public void testPutHashMapOfValues_shouldAssignValuesToVariables() {
    final Map<Variable, Double> map = Maps.newHashMap();
    map.put(PRICE_OPEN, 10d);
    map.put(PRICE_CLOSE, 20d);

    dataMap.putAll(map);
    assertThat(dataMap.size()).isEqualTo(2);
    assertThat(dataMap.get(PRICE_OPEN)).isEqualTo(10d);
    assertThat(dataMap.get(PRICE_CLOSE)).isEqualTo(20d);
  }

  @Test
  public void testPutAnotherDataMap_shouldAssignValuesToVariables() {
    final DataMap subDataMap = new DataMap();
    subDataMap.put(PRICE_OPEN, 10d);
    subDataMap.put(PRICE_CLOSE, 20d);

    dataMap.putAll(subDataMap);
    assertThat(dataMap.size()).isEqualTo(2);
    assertThat(dataMap.get(PRICE_OPEN)).isEqualTo(10d);
    assertThat(dataMap.get(PRICE_CLOSE)).isEqualTo(20d);
  }

  @Test
  public void testClear_shouldBeEmpty() {
    dataMap.put(PRICE_OPEN, 10);
    dataMap.put(PRICE_CLOSE, 20);
    dataMap.clear();

    assertThat(dataMap.size()).isEqualTo(0);
  }
}
