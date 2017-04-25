package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.entity.Algorithm;
import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.model.TradeStatistic;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.script.AlgorithmScriptFactory;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class StockScannerTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);

  private SimulationFactory simulationFactory;
  private Simulation simulation;
  private PriceDao priceDao;
  private AlgorithmDao tradeAlgorithmDao;
  private TradeStatistic tradeStatistic;
  private AlgorithmScriptFactory algorithmScriptFactory;
  private Clock clock = TestUtil.fixedClock(now);

  private StockScanner stockScanner;

  @Before
  public void before() {
    simulationFactory = mock(SimulationFactory.class);
    simulation = mock(Simulation.class);
    priceDao = mock(PriceDao.class);
    tradeAlgorithmDao = mock(AlgorithmDao.class);
    tradeStatistic = mock(TradeStatistic.class);
    algorithmScriptFactory = mock(AlgorithmScriptFactory.class);

    when(priceDao.findAllSymbols()).thenReturn(Sets.newHashSet("MEG"));
    when(simulationFactory.create(any())).thenReturn(simulation);

    stockScanner = new StockScanner(simulationFactory, priceDao, tradeAlgorithmDao, algorithmScriptFactory, clock);
  }

  @Test
  public void testScan_shouldRunSimulationOnAllSymbols() {
    when(tradeAlgorithmDao.findByAlgorithmId(anyString())).thenReturn(Optional.of(mock(Algorithm.class)));
    when(simulation.play())
        .thenReturn(SimulationReport.builder()
            .startingCash(100000)
            .symbol("MEG")
            .tradeStatistics(tradeStatistic)
            .addTransaction(BuyTransaction.buy().symbol("MEG").date(now).build())
            .endingCash(300000)
            .build());

    final List<ScanResult> scanResults = stockScanner.scan(ScanRequest.withAlgoId("algoId"));
    assertThat(scanResults).hasSize(1);

    final ScanResult scanResult = scanResults.get(0);
    assertThat(scanResult.getSymbol()).isEqualTo("MEG");
    assertThat(scanResult.getProfit()).isEqualTo(200000d);
    assertThat(scanResult.getProfitPcnt()).isEqualTo(2d);
    assertThat(scanResult.getLastTransaction().getSymbol()).isEqualTo("MEG");
    assertThat(scanResult.getLastTransaction().getTransactionType()).isEqualTo(TransactionType.BUY);
  }
}
