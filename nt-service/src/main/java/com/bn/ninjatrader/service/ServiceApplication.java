package com.bn.ninjatrader.service;

import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.process.guice.NtProcessModule;
import com.bn.ninjatrader.service.guice.NtServiceModule;
import com.bn.ninjatrader.service.health.ServiceHealthCheck;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.bn.ninjatrader.service.resource.*;
import com.bn.ninjatrader.service.task.CalcTask;
import com.bn.ninjatrader.service.task.ImportCSVPriceTask;
import com.bn.ninjatrader.service.task.ImportPSEDailyQuotesTask;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.AdminEnvironment;
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
  private IchimokuResource ichimokuResource;
  @Inject
  private SMAResource smaResource;
  @Inject
  private EMAResource emaResource;
  @Inject
  private RSIResource rsiResource;
  @Inject
  private MeanResource meanResource;
  @Inject
  private SimulationResource simulationResource;

  @Inject
  private CalcTask calcTask;

  @Inject
  private ImportCSVPriceTask importPriceTask;
  @Inject
  private ImportPSEDailyQuotesTask importPSEDailyQuotesTask;

  @Override
  public void run(ServiceConfig serviceConfig, Environment env) throws Exception {
    env.healthChecks().register("health", serviceHealthCheck);
    env.jersey().packages(LocalDateParamConverterProvider.class.getPackage().getName());

    setupResources(env.jersey());
    setupAdmin(env.admin());
    setupFilters(env.servlets());
  }

  private void setupResources(final JerseyEnvironment jersey) {
    jersey.register(priceResource);
    jersey.register(ichimokuResource);
    jersey.register(smaResource);
    jersey.register(emaResource);
    jersey.register(rsiResource);
    jersey.register(meanResource);
    jersey.register(simulationResource);
  }

  private void setupAdmin(final AdminEnvironment admin) {
    admin.addTask(calcTask);
    admin.addTask(importPriceTask);
    admin.addTask(importPSEDailyQuotesTask);
  }

  private void setupFilters(final ServletEnvironment servlet) {
    servlet.addFilter("crossOrigin", CrossOriginFilter.class)
        .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
  }

  public static void main(String[] args) throws Exception {
    final Injector injector = Guice.createInjector(
        new NtModelModule(),
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
