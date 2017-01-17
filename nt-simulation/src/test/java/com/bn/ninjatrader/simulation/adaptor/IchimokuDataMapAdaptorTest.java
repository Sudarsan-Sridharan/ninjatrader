package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.simulation.data.DataMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/18/16.
 */
public class IchimokuDataMapAdaptorTest {

  private IchimokuDataMapAdaptor adaptor;
  private LocalDate now;

  @BeforeMethod
  public void setup() {
    adaptor = new IchimokuDataMapAdaptor();
    now = LocalDate.of(2016, 1, 1);
  }

  @Test
  public void testToDataMap() {
    Ichimoku ichimoku = new Ichimoku(now, 1, 2, 3, 4, 5);
    DataMap dataMap = adaptor.toDataMap(ichimoku);

    assertThat(dataMap.get(ICHIMOKU_CHIKOU)).isEqualTo(ichimoku.getChikou());
    assertThat(dataMap.get(ICHIMOKU_TENKAN)).isEqualTo(ichimoku.getTenkan());
    assertThat(dataMap.get(ICHIMOKU_KIJUN)).isEqualTo(ichimoku.getKijun());
    assertThat(dataMap.get(ICHIMOKU_SENKOU_A)).isEqualTo(ichimoku.getSenkouA());
    assertThat(dataMap.get(ICHIMOKU_SENKOU_B)).isEqualTo(ichimoku.getSenkouB());
  }
}
