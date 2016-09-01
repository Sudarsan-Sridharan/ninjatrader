package com.bn.ninjatrader.testplay.simulation.adaptor;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.testplay.simulation.adaptor.IchimokuDataMapAdaptor;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.testplay.simulation.data.DataType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/18/16.
 */
public class IchimokuDataMapAdaptorTest {

  private IchimokuDataMapAdaptor adaptor;
  private LocalDate now;

  @BeforeClass
  public void setup() {
    adaptor = new IchimokuDataMapAdaptor();
    now = LocalDate.of(2016, 1, 1);
  }

  @Test
  public void testToDataMap() {
    Ichimoku ichimoku = new Ichimoku(now, 1, 2, 3, 4, 5);
    DataMap dataMap = adaptor.toDataMap(ichimoku);

    assertEquals(dataMap.get(DataType.CHIKOU), ichimoku.getChikou());
    assertEquals(dataMap.get(DataType.TENKAN), ichimoku.getTenkan());
    assertEquals(dataMap.get(DataType.KIJUN), ichimoku.getKijun());
    assertEquals(dataMap.get(DataType.SENKOU_A), ichimoku.getSenkouA());
    assertEquals(dataMap.get(DataType.SENKOU_B), ichimoku.getSenkouB());
  }
}
