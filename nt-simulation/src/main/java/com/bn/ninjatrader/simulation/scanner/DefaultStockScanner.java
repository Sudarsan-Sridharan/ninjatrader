package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScript;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScriptFactory;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.exception.AlgorithmNotFoundException;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class DefaultStockScanner implements StockScanner {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultStockScanner.class);

  private final SimulationFactory simulationFactory;
  private final PriceDao priceDao;
  private final AlgorithmDao tradeAlgorithmDao;
  private final AlgorithmScriptFactory algorithmScriptFactory;
  private final Clock clock;
  private final SimulationReportConverter simulationReportConverter;

  @Inject
  public DefaultStockScanner(final SimulationFactory simulationFactory,
                             final PriceDao priceDao,
                             final AlgorithmDao tradeAlgorithmDao,
                             final AlgorithmScriptFactory algorithmScriptFactory,
                             final Clock clock,
                             final SimulationReportConverter simulationReportConverter) {
    this.simulationFactory = simulationFactory;
    this.priceDao = priceDao;
    this.tradeAlgorithmDao = tradeAlgorithmDao;
    this.algorithmScriptFactory = algorithmScriptFactory;
    this.clock = clock;
    this.simulationReportConverter = simulationReportConverter;
  }

  @Override
  public Map<String, ScanResult> scan(final ScanRequest req) {
    final List<Simulation> simulations = prepareSimulations(req);
    final List<SimulationReport> reports = simulations.stream()
        .map(simulation -> simulation.play())
        .filter(report -> {
          if (!report.getTransactions().isEmpty()) {
            final Transaction txn = report.getTransactions().get(report.getTransactions().size() - 1);
            if (txn.getDate().plusDays(30).isAfter(LocalDate.now(clock))) {
              return true;
            }
          }
          return false;
        }).collect(Collectors.toList());

    // Scan each report and collect their last transaction
    final Map<String, ScanResult> scanResults = simulationReportConverter.convert(reports);

    return scanResults;
  }

  /**
   * Either find all symbols or use given symbols from request.
   */
  private Collection<String> prepareSymbols(final ScanRequest req) {
    final Collection<String> symbols;
    if (req.getSymbols() == null || req.getSymbols().isEmpty()) {
      symbols = priceDao.findAllSymbols();
    } else {
      symbols = req.getSymbols();
    }
    return symbols;
  }

  /**
   * Prepare simulation for each symbol.
   */
  private List<Simulation> prepareSimulations(final ScanRequest req) {
    final LocalDate from = LocalDate.now(clock).minusYears(1);
    final LocalDate to = LocalDate.now(clock);

    // Get stock symbols to scan
    final Collection<String> symbols = prepareSymbols(req);

    // Prepare algorithm script to scan with
    final AlgorithmScript algoScript = prepareAlgorithmScript(req);

    // Play simulation and collect reports for each symbol
    return symbols.stream()
        .map(symbol -> simulationFactory.create(SimulationRequest.withSymbol(symbol)
            .from(from).to(to).algorithmScript(algoScript)))
        .collect(Collectors.toList());

  }

  /**
   * Either retrieve algorithm from database or use the given one from request.
   */
  private AlgorithmScript prepareAlgorithmScript(final ScanRequest req) {
    final Algorithm algorithm = findAlgorithmById(req.getAlgorithmId());
    final AlgorithmScript algoScript = algorithmScriptFactory.create(algorithm);
    return algoScript;
  }

  /**
   * Find algorithm from database with given algorithmId
   */
  private Algorithm findAlgorithmById(final String algorithmId) {
    return tradeAlgorithmDao.findOneByAlgorithmId(algorithmId)
        .orElseThrow(() -> new AlgorithmNotFoundException(algorithmId));
  }
}
