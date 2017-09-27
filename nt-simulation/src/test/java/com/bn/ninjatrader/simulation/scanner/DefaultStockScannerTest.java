package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScriptFactory;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.model.stat.TradeStatistic;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class DefaultStockScannerTest {

  private final LocalDate now = LocalDate.of(2016, 2, 2);
  private final LocalDate yesterday = now.minusDays(1);

  private SimulationFactory simulationFactory;
  private Simulation simulation;
  private PriceDao priceDao;
  private AlgorithmDao tradeAlgorithmDao;
  private TradeStatistic tradeStatistic;
  private AlgorithmScriptFactory algorithmScriptFactory;
  private Clock clock = TestUtil.fixedClock(now);
  private SimulationReportConverter simulationReportConverter;

  private StockScanner stockScanner;

  @Before
  public void before() {
    simulationFactory = mock(SimulationFactory.class);
    simulation = mock(Simulation.class);
    priceDao = mock(PriceDao.class);
    tradeAlgorithmDao = mock(AlgorithmDao.class);
    tradeStatistic = mock(TradeStatistic.class);
    algorithmScriptFactory = mock(AlgorithmScriptFactory.class);
    simulationReportConverter = mock(SimulationReportConverter.class);

    when(priceDao.findAllSymbols()).thenReturn(Sets.newHashSet("MEG"));
    when(tradeAlgorithmDao.findOneByAlgorithmId(anyString())).thenReturn(Optional.of(mock(Algorithm.class)));
    when(simulationFactory.create(any())).thenReturn(simulation);
    when(simulation.play())
        .thenReturn(SimulationReport.builder()
            .startingCash(100000)
            .symbol("MEG")
            .tradeStatistics(tradeStatistic)
            .addTransaction(BuyTransaction.buy().symbol("MEG").date(now).build())
            .addTransaction(BuyTransaction.buy().symbol("MEG").date(yesterday).build())
            .endingCash(300000)
            .profit(200000)
            .profitPcnt(2)
            .build());
    when(simulationReportConverter.convert(any())).thenCallRealMethod();

    stockScanner = new DefaultStockScanner(simulationFactory, priceDao,
        tradeAlgorithmDao, algorithmScriptFactory, clock, simulationReportConverter);
  }

  @Test
  public void testScan_shouldRunSimulationOnAllSymbols() {
    final Map<String, ScanResult> scanResults = stockScanner.scan(ScanRequest.withAlgoId("algoId"));
    assertThat(scanResults).hasSize(1);

    final ScanResult scanResult = scanResults.get("MEG");
    assertThat(scanResult.getSymbol()).isEqualTo("MEG");
    assertThat(scanResult.getProfit()).isEqualTo(200000d);
    assertThat(scanResult.getProfitPcnt()).isEqualTo(2d);
    assertThat(scanResult.getLastTransaction().getSymbol()).isEqualTo("MEG");
    assertThat(scanResult.getLastTransaction().getTransactionType()).isEqualTo(TransactionType.BUY);
  }

  @Test
  public void testScanWithSymbols_shouldRunSimulationOnGivenSymbols() {
    when(simulation.play())
        .thenReturn(
            SimulationReport.builder().startingCash(1000).symbol("TEL")
                .addTransaction(BuyTransaction.buy().symbol("TEL").date(now).build())
                .build(),
            SimulationReport.builder().startingCash(1000).symbol("BDO")
                .addTransaction(BuyTransaction.buy().symbol("BDO").date(now).build())
                .build());

    final ArgumentCaptor<SimulationRequest> captor = ArgumentCaptor.forClass(SimulationRequest.class);
    final Map<String, ScanResult> scanResults = stockScanner.scan(ScanRequest.withAlgoId("algoId")
        .symbols(Lists.newArrayList("TEL", "BDO")));
    assertThat(scanResults).hasSize(2);

    verify(simulationFactory, times(2)).create(captor.capture());

    assertThat(captor.getAllValues().stream().map(r -> r.getSymbol())).containsExactlyInAnyOrder("TEL", "BDO");
  }

  @Test
  public void testScanWithDayParameter_shouldFilterOutResultsOlderThanGivenDay() {
    final Map<String, ScanResult> scanResults = stockScanner.scan(ScanRequest.withAlgoId("algoId"));

    assertThat(scanResults).hasSize(1);
    assertThat(scanResults.get("MEG").getLastTransaction().getDate()).isEqualTo(now);
  }
}
