package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.common.model.Algorithm;
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
    final Algorithm algorithm = Algorithm.builder()
        .algorithm("SAMPLE_SCRIPT").userId("test").algoId("algoId").build();
    when(tradeAlgorithmDao.findOneByAlgorithmId("algoId")).thenReturn(Optional.of(algorithm));

    final AlgorithmScript script = service.findById("algoId");
  }
}
