package com.bn.ninjatrader.simulation.data.provider;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.model.dao.IchimokuDao;
import com.bn.ninjatrader.simulation.adaptor.IchimokuDataMapAdaptor;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.core.SimulationData;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.simulation.operation.Variables.ICHIMOKU_KIJUN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 9/1/16.
 */
public class IchimokuDataProviderTest {

  private final LocalDate fromDate = LocalDate.of(2016, 2, 2);
  private final LocalDate toDate = LocalDate.of(2016, 4, 30);
  private final Ichimoku ichimoku = new Ichimoku(fromDate, 1, 2, 3, 4, 5);

  private SimulationParams params;
  private DataMap dataMap;
  private IchimokuDataProvider dataFinder;
  private IchimokuDao ichimokuDao;
  private IchimokuDataMapAdaptor dataMapAdaptor;

  @Before
  public void setup() {
    ichimokuDao = mock(IchimokuDao.class);
    dataMapAdaptor = mock(IchimokuDataMapAdaptor.class);
    dataFinder = new IchimokuDataProvider(ichimokuDao, dataMapAdaptor);

    params = new SimulationParams();
    params.setSymbol("MEG");
    params.setFromDate(fromDate);
    params.setToDate(toDate);

    dataMap = new DataMap();
    dataMap.put(ICHIMOKU_KIJUN, ichimoku.getKijun());

    when(ichimokuDao.find(any())).thenReturn(Lists.newArrayList(ichimoku));
    when(dataMapAdaptor.toDataMap(ichimoku)).thenReturn(dataMap);
  }

  @Test
  public void testFind_shouldReturnSimulationData() {
    final int requiredSize = 10;
    final List<SimulationData<Ichimoku>> dataList = dataFinder.find(params, requiredSize);
    assertThat(dataList).isNotNull().hasSize(1);

    // Should return SimulationData with the required size.
    final SimulationData<Ichimoku> simulationData = dataList.get(0);
    assertThat(simulationData.size()).isEqualTo(requiredSize);

    // Verify variable values are correct.
    final DataMap dataMap = simulationData.getDataAtIndex(requiredSize - 1);
    assertThat(dataMap).isNotNull().hasSize(1);
    assertThat(dataMap.get(ICHIMOKU_KIJUN)).isEqualTo(ichimoku.getKijun());
  }
}
