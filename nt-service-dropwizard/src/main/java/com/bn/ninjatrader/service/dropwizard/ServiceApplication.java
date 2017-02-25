package com.bn.ninjatrader.service.dropwizard;

import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.process.guice.NtProcessModule;
import com.bn.ninjatrader.service.dropwizard.health.ServiceHealthCheck;
import com.bn.ninjatrader.service.guice.NtServiceModule;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.bn.ninjatrader.service.resource.*;
import com.bn.ninjatrader.service.task.*;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * @author bradwee2000@gmail.com
 */
public class ServiceApplication extends Application<ServiceConfig> {

  @Inject
  private ServiceHealthCheck serviceHealthCheck;

  @Inject
  private PriceResource priceResource;
  @Inject
  private SimulationResource simulationResource;

  @Inject
  private CalcTask calcTask;
  @Inject
  private RunSimulationTask runSimulationTask;
  @Inject
  private RunStockScannerTask runStockScannerTask;
  @Inject
  private ImportCSVPriceTask importPriceTask;
  @Inject
  private ImportPSEDailyQuotesTask importPSEDailyQuotesTask;
  @Inject
  private ImportPSETraderDailyQuotesTask importPSETraderDailyQuotesTask;

  @Override
  public void run(final ServiceConfig serviceConfig, final Environment env) throws Exception {
    env.healthChecks().register("health", serviceHealthCheck);
    env.jersey().packages(LocalDateParamConverterProvider.class.getPackage().getName());

    setupResources(env.jersey());
    setupFilters(env.servlets());
  }

  private void setupResources(final JerseyEnvironment jersey) {
    jersey.register(priceResource);
    jersey.register(simulationResource);

    // Maintenance tasks
    jersey.register(calcTask);
    jersey.register(runSimulationTask);
    jersey.register(runStockScannerTask);
    jersey.register(importPriceTask);
    jersey.register(importPSEDailyQuotesTask);
    jersey.register(importPSETraderDailyQuotesTask);
  }

  private void setupFilters(final ServletEnvironment servlet) {
    servlet.addFilter("crossOrigin", CrossOriginFilter.class)
        .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
  }

  public static void main(String[] args) throws Exception {
    final Injector injector = Guice.createInjector(
        new NtModelMongoModule(),
        new NtServiceModule(),
        new NtProcessModule(),
        new NtSimulationModule()
    );
    final ServiceApplication serviceApplication = injector.getInstance(ServiceApplication.class);

    if (args.length == 0) {
      args = new String[] {"server"};
    }

    serviceApplication.run(args);
  }
}
