package com.bn.ninjatrader.service.dropwizard;

import com.bn.ninjatrader.cache.client.api.CacheClient;
import com.bn.ninjatrader.messaging.MessagingClient;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.scheduler.JobScheduler;
import com.bn.ninjatrader.scheduler.guice.NtSchedulerModule;
import com.bn.ninjatrader.service.dropwizard.health.ServiceHealthCheck;
import com.bn.ninjatrader.service.dropwizard.lifecycle.ManagedCacheClient;
import com.bn.ninjatrader.service.dropwizard.lifecycle.ManagedJobScheduler;
import com.bn.ninjatrader.service.dropwizard.lifecycle.ManagedMessagingClient;
import com.bn.ninjatrader.service.dropwizard.lifecycle.ManagedWorkerClient;
import com.bn.ninjatrader.service.exception.JsonParseExceptionMapper;
import com.bn.ninjatrader.service.filter.AuthorizationFilter;
import com.bn.ninjatrader.service.filter.CrossOriginResourceResponseFilter;
import com.bn.ninjatrader.service.filter.EventDispatchFilter;
import com.bn.ninjatrader.service.guice.NtServiceModule;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.bn.ninjatrader.service.provider.ObjectMapperContextResolver;
import com.bn.ninjatrader.service.resource.AlgorithmResource;
import com.bn.ninjatrader.service.resource.PriceResource;
import com.bn.ninjatrader.service.resource.ScanResource;
import com.bn.ninjatrader.service.resource.TestEventResource;
import com.bn.ninjatrader.service.resource.UserResource;
import com.bn.ninjatrader.service.task.ImportCSVPriceTask;
import com.bn.ninjatrader.service.task.ImportPSEDailyQuotesTask;
import com.bn.ninjatrader.service.task.ImportPSETraderDailyQuotesTask;
import com.bn.ninjatrader.service.task.PriceAdjustmentTask;
import com.bn.ninjatrader.service.task.RenameStockSymbolTask;
import com.bn.ninjatrader.service.task.RunSimulationTask;
import com.bn.ninjatrader.service.task.RunStockScannerTask;
import com.bn.ninjatrader.worker.WorkerDispatcher;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

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
  private ScanResource scanResource;
  @Inject
  private TestEventResource testEventResource;

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
  private RenameStockSymbolTask renameStockSymbolTask;
  @Inject
  private ImportPSETraderDailyQuotesTask importPSETraderDailyQuotesTask;
  @Inject
  private ObjectMapperContextResolver objectMapperContextResolver;

  @Inject
  private CrossOriginResourceResponseFilter crossOriginResourceResponseFilter;
  @Inject
  private AuthorizationFilter authorizationFilter;
  @Inject
  private EventDispatchFilter eventDispatchFilter;

  @Inject
  private JobScheduler jobScheduler;
  @Inject
  private CacheClient cacheClient;
  @Inject
  private MessagingClient messagingClient;
  @Inject
  private WorkerDispatcher workerDispatcher;

  @Override
  public void initialize(Bootstrap<ServiceConfig> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
  }

  @Override
  public void run(final ServiceConfig serviceConfig, final Environment env) throws Exception {
    env.healthChecks().register("health", serviceHealthCheck);

    registerProviders(env.jersey());

    setupResources(env.jersey());

    env.lifecycle().manage(new ManagedJobScheduler(jobScheduler));
    env.lifecycle().manage(new ManagedWorkerClient(workerDispatcher, ""));
    env.lifecycle().manage(new ManagedCacheClient(cacheClient, ""));
    env.lifecycle().manage(new ManagedMessagingClient(messagingClient, ""));
  }

  private void registerProviders(final JerseyEnvironment jersey) {
    jersey.packages(LocalDateParamConverterProvider.class.getPackage().getName());
    jersey.packages(JsonParseExceptionMapper.class.getPackage().getName());
  }

  private void setupResources(final JerseyEnvironment jersey) {
    jersey.register(priceResource);
    jersey.register(algorithmResource);
    jersey.register(userResource);
    jersey.register(scanResource);
    jersey.register(testEventResource);

    // Maintenance tasks
    jersey.register(runSimulationTask);
    jersey.register(runStockScannerTask);
    jersey.register(importPriceTask);
    jersey.register(importPSEDailyQuotesTask);
    jersey.register(importPSETraderDailyQuotesTask);
    jersey.register(priceAdjustmentTask);
    jersey.register(renameStockSymbolTask);

    // ObjectMapper
    jersey.register(objectMapperContextResolver);

    // Filters
    jersey.register(crossOriginResourceResponseFilter);
    jersey.register(authorizationFilter);
    jersey.register(eventDispatchFilter);

    // Features
    jersey.register(SseFeature.class);
  }

  public static void main(String[] args) throws Exception {
    LOG.info("Starting Service Application");

    final Injector injector = Guice.createInjector(
        new NtSchedulerModule(),
        new NtServiceModule(),
        new NtModelMongoModule()
    );

    final ServiceApplication serviceApplication = injector.getInstance(ServiceApplication.class);

    if (args.length == 0) {
      args = new String[] {"server", "service-config.yaml"};
    }
    serviceApplication.run(args);




//    final PriceDao priceDao = injector.getInstance(PriceDao.class);
//
//    final int skip = 130;
//
//    final List<String> symbols = priceDao.findAllSymbols().stream().sorted().skip(skip).limit(500).collect(Collectors.toList());
//
//    ObjectMapperContextResolver objectMapperContextResolver = injector.getInstance(ObjectMapperContextResolver.class);
//
//    symbols.forEach(symbol -> {
//      final List<Price> prices = priceDao.findPrices().withSymbol(symbol).from(LocalDate.now().minusYears(50)).to(LocalDate.now()).now();
//      LOG.info("Found {} prices for {}", prices.size(), symbol);
//      final int statusCode = ClientBuilder.newClient().register(objectMapperContextResolver)
//          .target("https://beach-ninja.appspot.com/migrate/price/"+symbol).request().post(Entity.json(prices)).getStatus();
//      LOG.info("STATUS CODE: {} {}", symbol, statusCode);
//    });
  }
}
