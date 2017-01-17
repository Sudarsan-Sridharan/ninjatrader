package com.bn.ninjatrader.simulation.data;

import org.testng.annotations.Test;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/11/16.
 */
public class BarDataTest {

  @Test
  public void testPutDataMap_shouldSetValuesToVariables() {
    DataMap dataMap = new DataMap();
    dataMap.put(ICHIMOKU_CHIKOU, 100d);
    dataMap.put(ICHIMOKU_TENKAN, 200d);

    BarData barData = new BarData().put(dataMap);

    assertThat(barData.get(ICHIMOKU_CHIKOU)).isEqualTo(100d);
    assertThat(barData.get(ICHIMOKU_TENKAN)).isEqualTo(200d);
  }

  @Test
  public void testOverwriteDataMap_shouldOvrwriteVariableValues() {
    DataMap dataMap = new DataMap();
    dataMap.put(ICHIMOKU_CHIKOU, 100d);
    dataMap.put(ICHIMOKU_TENKAN, 200d);

    BarData barData = new BarData().put(dataMap);

    DataMap overlayDataMap = new DataMap();
    overlayDataMap.put(ICHIMOKU_TENKAN, 300d);
    overlayDataMap.put(ICHIMOKU_KIJUN, 400d);

    barData.put(overlayDataMap);

    assertThat(barData.get(ICHIMOKU_CHIKOU)).isEqualTo(100d);
    assertThat(barData.get(ICHIMOKU_TENKAN)).isEqualTo(300d);
    assertThat(barData.get(ICHIMOKU_KIJUN)).isEqualTo(400d);
  }
}
