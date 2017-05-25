package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
import org.junit.Test;

import static com.bn.ninjatrader.simulation.logic.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/11/16.
 */
public class BarDataTest {

  private final PriceBuilderFactory pbf = new DummyPriceBuilderFactory();

  @Test
  public void testBuild_shouldSetProperties() {
    final Price price = pbf.builder().close(1).build();
    final BarData barData = BarData.builder().price(price).index(10).symbol("MEG").build();

    assertThat(barData).isNotNull();
    assertThat(barData.getSymbol()).isEqualTo("MEG");
    assertThat(barData.getPrice()).isEqualTo(price);
    assertThat(barData.getIndex()).isEqualTo(10);
  }

  @Test
  public void testPutDataMap_shouldSetValuesToVariables() {
    final DataMap dataMap = DataMap.newInstance()
        .addData(ICHIMOKU_CHIKOU, 100d)
        .addData(ICHIMOKU_TENKAN, 200d);
    final BarData barData = BarData.builder().addData(dataMap).build();

    assertThat(barData.get(ICHIMOKU_CHIKOU)).isEqualTo(100d);
    assertThat(barData.get(ICHIMOKU_TENKAN)).isEqualTo(200d);
  }

  @Test
  public void testOverwriteDataMap_shouldOverwriteVariableValues() {
    final DataMap dataMap = DataMap.newInstance()
        .addData(ICHIMOKU_CHIKOU, 100d)
        .addData(ICHIMOKU_TENKAN, 200d);
    final DataMap overlayDataMap = DataMap.newInstance()
        .addData(ICHIMOKU_TENKAN, 300d)
        .addData(ICHIMOKU_KIJUN, 400d);
    final BarData barData = BarData.builder().addData(dataMap).addData(overlayDataMap).build();

    assertThat(barData.get(ICHIMOKU_CHIKOU)).isEqualTo(100d);
    assertThat(barData.get(ICHIMOKU_TENKAN)).isEqualTo(300d);
    assertThat(barData.get(ICHIMOKU_KIJUN)).isEqualTo(400d);
  }
}
