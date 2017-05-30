package com.bn.ninjatrader.service.dropwizard;

import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.scheduler.JobScheduler;
import com.bn.ninjatrader.scheduler.guice.NtSchedulerModule;
import com.bn.ninjatrader.service.dropwizard.health.ServiceHealthCheck;
import com.bn.ninjatrader.service.exception.JsonParseExceptionMapper;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.bn.ninjatrader.service.provider.ObjectMapperContextResolver;
import com.bn.ninjatrader.service.resource.AlgorithmResource;
import com.bn.ninjatrader.service.resource.PriceResource;
import com.bn.ninjatrader.service.resource.UserResource;
import com.bn.ninjatrader.service.task.ImportCSVPriceTask;
import com.bn.ninjatrader.service.task.ImportPSEDailyQuotesTask;
import com.bn.ninjatrader.service.task.ImportPSETraderDailyQuotesTask;
import com.bn.ninjatrader.service.task.PriceAdjustmentTask;
import com.bn.ninjatrader.service.task.RunSimulationTask;
import com.bn.ninjatrader.service.task.RunStockScannerTask;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.netflix.archaius.api.Config;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * @author bradwee2000@gmail.com
 */
public class ServiceApplication extends Application<ServiceConfig> {
  private static final Logger LOG = LoggerFactory.getLogger(ServiceApplication.class);

  @Inject
  private ServiceHealthCheck serviceHealthCheck;

  @Inject
  private PriceResource priceResource;
  @Inject
  private AlgorithmResource algorithmResource;
  @Inject
  private UserResource userResource;

  @Inject
  private RunSimulationTask runSimulationTask;
  @Inject
  private RunStockScannerTask runStockScannerTask;
  @Inject
  private PriceAdjustmentTask priceAdjustmentTask;
  @Inject
  private ImportCSVPriceTask importPriceTask;
  @Inject
  private ImportPSEDailyQuotesTask importPSEDailyQuotesTask;
  @Inject
  private ImportPSETraderDailyQuotesTask importPSETraderDailyQuotesTask;
  @Inject
  private ObjectMapperContextResolver objectMapperContextResolver;

  @Inject
  private JobScheduler jobScheduler;

  @Override
  public void initialize(Bootstrap<ServiceConfig> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
  }

  @Override
  public void run(final ServiceConfig serviceConfig, final Environment env) throws Exception {
    env.healthChecks().register("health", serviceHealthCheck);

    registerProviders(env.jersey());

    setupResources(env.jersey());
    setupFilters(env.servlets());

    env.lifecycle().manage(new Managed() {
      @Override
      public void start() throws Exception {
        jobScheduler.start();
      }

      @Override
      public void stop() throws Exception {
        jobScheduler.shutdown();
      }
    });
  }

  private void registerProviders(final JerseyEnvironment jersey) {
    jersey.packages(LocalDateParamConverterProvider.class.getPackage().getName());
    jersey.packages(JsonParseExceptionMapper.class.getPackage().getName());
  }

  private void setupResources(final JerseyEnvironment jersey) {
    jersey.register(priceResource);
    jersey.register(algorithmResource);
    jersey.register(userResource);


    // Maintenance tasks
    jersey.register(runSimulationTask);
    jersey.register(runStockScannerTask);
    jersey.register(importPriceTask);
    jersey.register(importPSEDailyQuotesTask);
    jersey.register(importPSETraderDailyQuotesTask);
    jersey.register(priceAdjustmentTask);

    // ObjectMapper
    jersey.register(objectMapperContextResolver);
  }

  private void setupFilters(final ServletEnvironment servlet) {
    servlet.addFilter("crossOrigin", CrossOriginFilter.class)
        .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
  }

  public static void main(String[] args) throws Exception {
    final Injector injector = Guice.createInjector(
        new NtModelMongoModule(),
        new NtSimulationModule(),
        new NtSchedulerModule()
    );

    final ServiceApplication serviceApplication = injector.getInstance(ServiceApplication.class);

    final Config config = injector.getInstance(Config.class);
    Lists.newArrayList(config.getKeys()).stream()
        .forEach(key -> System.out.println("-- " + key + " = " + config.getString(key)));

    if (args.length == 0) {
      args = new String[] {"server", "service-config.yaml"};
    }
    serviceApplication.run(args);
  }
}
