package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.simulation.logic.Variable;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.simulation.binding.BindingFactory;
import com.bn.ninjatrader.simulation.binding.BindingProvider;
import com.bn.ninjatrader.simulation.data.BarProducer;
import com.bn.ninjatrader.simulation.model.SimulationContext;
import com.bn.ninjatrader.simulation.model.SimContextFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class SimulationFactory {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationFactory.class);

  private final PriceDao priceDao;
  private final BindingFactory varCalculatorFactory;
  private final SimContextFactory simContextFactory;
  private final Clock clock;

  @Inject
  public SimulationFactory(final BindingFactory varCalculatorFactory,
                           final PriceDao priceDao,
                           final SimContextFactory simContextFactory,
                           final Clock clock) {
    this.priceDao = priceDao;
    this.varCalculatorFactory = varCalculatorFactory;
    this.simContextFactory = simContextFactory;
    this.clock = clock;
  }

  public Simulation create(final SimulationRequest req) {
    checkNotNull(req, "SimulationRequest must not be null.");
    checkNotNull(req.getAlgorithmScript(), "SimulationRequest.algorithmScript must not be null");

    // Set default values
    setRequestDefaultsIfNull(req);

    // Provides data for different variables
    final Collection<BindingProvider> bindingProviders =
        varCalculatorFactory.createForVariables(req.getAlgorithmScript().getVariables());

    // Producer for bars
    final BarProducer barProducer = new BarProducer(bindingProviders);

    final SimulationContext context = simContextFactory.create(req);
    final Simulation simulation = new Simulation(context, req, barProducer);

    // Pre calculate indicators that require many past days of data
    final int biggestPeriod = findBiggestPeriod(req.getAlgorithmScript().getVariables());
    final List<Price> preDatePrices = priceDao.findBeforeDate(FindBeforeDateRequest.builder()
        .symbol(req.getSymbol())
        .beforeDate(req.getFrom())
        .numOfValues(biggestPeriod)
        .timeFrame(TimeFrame.ONE_DAY)
        .build());

    barProducer.preCalc(preDatePrices);

    return simulation;
  }

  /**
   * Find the biggest period in the algorithm
   */
  private int findBiggestPeriod(final Collection<Variable> variables) {
    int biggestPeriod = 0;
    for (final Variable variable : variables) {
      if (biggestPeriod < variable.getPeriod()) {
        biggestPeriod = variable.getPeriod();
      }
    }
    return biggestPeriod;
  }

  /**
   * Prepare date range and starting cash defaults if not given.
   */
  private void setRequestDefaultsIfNull(final SimulationRequest req) {
    req.from(Optional.ofNullable(req.getFrom()).orElse(LocalDate.now(clock).minusYears(1)));
    req.to(Optional.ofNullable(req.getTo()).orElse(LocalDate.now(clock)));
    req.startingCash(req.getStartingCash() == 0d ? 100_000 :req.getStartingCash());
  }
}
