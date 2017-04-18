package com.bn.ninjatrader.simulation.service;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.simulation.script.AlgorithmScriptFactory;
import com.bn.ninjatrader.simulation.script.AlgorithmScript;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class AlgorithmServiceTest {

  private AlgorithmDao tradeAlgorithmDao;
  private AlgorithmScriptFactory scriptFactory;

  private AlgorithmService service;

  @Before
  public void before() {
    tradeAlgorithmDao = mock(AlgorithmDao.class);
    scriptFactory = mock(AlgorithmScriptFactory.class);
    service = new AlgorithmService(tradeAlgorithmDao, scriptFactory);
  }

  @Test
  public void testFind_shouldReturnSimTradeAlgorithm() {
    final TradeAlgorithm tradeAlgorithm = TradeAlgorithm.builder()
        .algorithm("SAMPLE_SCRIPT").userId("test").algoId("algoId").build();
    when(tradeAlgorithmDao.findByTradeAlgorithmId("algoId")).thenReturn(Optional.of(tradeAlgorithm));

    final AlgorithmScript script = service.findById("algoId");
  }
}
