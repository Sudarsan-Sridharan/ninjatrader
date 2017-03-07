package com.bn.ninjatrader.simulation.service;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.entity.TradeAlgorithmFactory;
import com.bn.ninjatrader.model.request.FindTradeAlgorithmRequest;
import com.bn.ninjatrader.model.request.SaveTradeAlgorithmRequest;
import com.bn.ninjatrader.simulation.jackson.SimObjectMapperProvider;
import com.bn.ninjatrader.simulation.logicexpression.statement.EmptyStatement;
import com.bn.ninjatrader.simulation.model.SimTradeAlgorithm;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class SimTradeAlgorithmServiceTest {

  private final SimTradeAlgorithm algo = SimTradeAlgorithm.builder().play(EmptyStatement.instance()).build();
  private static final String jsonAlgo = "{\"init\":{\"_t\":\"empty\"}," +
      "\"play\":{\"_t\":\"empty\"}," +
      "\"onBuyFulfilled\":{\"_t\":\"empty\"}," +
      "\"onSellFulfilled\":{\"_t\":\"empty\"}}";

  private TradeAlgorithmDao tradeAlgorithmDao;
  private TradeAlgorithmFactory tradeAlgorithmFactory;
  private SimObjectMapperProvider simObjectMapperProvider;

  private SimTradeAlgorithmService service;

  @Before
  public void before() {
    tradeAlgorithmDao = mock(TradeAlgorithmDao.class);
    tradeAlgorithmFactory = mock(TradeAlgorithmFactory.class);
    simObjectMapperProvider = new SimObjectMapperProvider();
    service = new SimTradeAlgorithmService(tradeAlgorithmDao, tradeAlgorithmFactory, simObjectMapperProvider);
  }

  @Test
  public void testSave_shouldSaveAsJson() {
    final TradeAlgorithm tradeAlgorithm = service.save(SaveSimTradeAlgoRequest.withAlgorithm(algo)
        .userId("test_user")
        .description("sample")
        .tradeAlgorithmId("algoId"));

    verify(tradeAlgorithmDao).save(any(SaveTradeAlgorithmRequest.class));

    assertThat(tradeAlgorithm).isNotNull();
    assertThat(tradeAlgorithm.getDescription()).isEqualTo("sample");
    assertThat(tradeAlgorithm.getUserId()).isEqualTo("test_user");
    assertThat(tradeAlgorithm.getId()).isEqualTo("algoId");
    assertThat(tradeAlgorithm.getAlgorithm()).isEqualTo(jsonAlgo);
  }

  @Test
  public void testFind_shouldReturnSimTradeAlgorithm() {
    final TradeAlgorithm tradeAlgorithm = TradeAlgorithm.builder()
        .algorithm(jsonAlgo).userId("test").id("algoId").build();
    when(tradeAlgorithmDao.findOne(any(FindTradeAlgorithmRequest.class))).thenReturn(Optional.of(tradeAlgorithm));

    final SimTradeAlgorithm simTradeAlgorithm = service.findById("algoId");

    assertThat(simTradeAlgorithm).isEqualTo(algo);
  }
}
