package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.simulation.Simulator;
import com.bn.ninjatrader.simulation.GoldenAlgorithm;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.model.TradeStatistic;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.service.SimTradeAlgorithmService;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

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
  private Simulator simulator;
  private PriceDao priceDao;
  private GoldenAlgorithm algo;
  private SimulationReport simulationReport;
  private SimulationParams simulationParams;
  private TradeStatistic tradeStatistic;
  private SimTradeAlgorithmService simTradeAlgorithmService;
  private Clock clock = TestUtil.fixedClock(now);

  private StockScanner stockScanner;

  @Before
  public void before() {
    simulator = mock(Simulator.class);
    priceDao = mock(PriceDao.class);
    algo = mock(GoldenAlgorithm.class);
    simulationReport = mock(SimulationReport.class);
    simulationParams = mock(SimulationParams.class);
    tradeStatistic = mock(TradeStatistic.class);
    simTradeAlgorithmService = mock(SimTradeAlgorithmService.class);

    when(priceDao.findAllSymbols()).thenReturn(Sets.newHashSet("MEG"));
    when(algo.forSymbol(anyString())).thenReturn(SimulationParams.builder());
    when(simulator.play(any(SimulationParams.class))).thenReturn(simulationReport);

    stockScanner = new StockScanner(simulator, priceDao, simTradeAlgorithmService, clock);
  }

  @Test
  public void testScan_shouldRunSimulationOnAllSymbols() {
    when(simulator.play(any(SimulationRequest.class)))
        .thenReturn(SimulationReport.builder()
            .startingCash(100000)
            .params(simulationParams)
            .tradeStatistics(tradeStatistic)
            .addTransaction(BuyTransaction.buy().symbol("MEG").date(now).build())
            .build());
    when(tradeStatistic.getTotalProfit()).thenReturn(200000d);
    when(simulationParams.getSymbol()).thenReturn("MEG");

    final List<ScanResult> scanResults = stockScanner.scan("algoId");
    assertThat(scanResults).hasSize(1);

    final ScanResult scanResult = scanResults.get(0);
    assertThat(scanResult.getSymbol()).isEqualTo("MEG");
    assertThat(scanResult.getProfit()).isEqualTo(200000d);
    assertThat(scanResult.getProfitPcnt()).isEqualTo(2d);
    assertThat(scanResult.getLastTransaction().getSymbol()).isEqualTo("MEG");
    assertThat(scanResult.getLastTransaction().getTransactionType()).isEqualTo(TransactionType.BUY);
  }
}
