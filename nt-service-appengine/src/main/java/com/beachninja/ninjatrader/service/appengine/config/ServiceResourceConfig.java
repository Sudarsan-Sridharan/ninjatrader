package com.beachninja.ninjatrader.service.appengine.config;

import com.beachninja.ninjatrader.service.appengine.cron.ImportQuotesCronJob;
import com.bn.ninjatrader.auth.guice.NtAuthModule;
import com.bn.ninjatrader.cache.client.api.CacheClient;
import com.bn.ninjatrader.cache.client.appengine.AppEngineCacheClient;
import com.bn.ninjatrader.common.guice.NtClockModule;
import com.bn.ninjatrader.common.model.DailyQuote;
import com.bn.ninjatrader.event.dispatcher.AppEngineMessagePublisher;
import com.bn.ninjatrader.event.dispatcher.MessagePublisher;
import com.bn.ninjatrader.messaging.guice.NtMessagingModule;
import com.bn.ninjatrader.model.datastore.guice.NtModelDatastoreModule;
import com.bn.ninjatrader.queue.TaskDispatcher;
import com.bn.ninjatrader.queue.appengine.AppEngineTaskDispatcher;
import com.bn.ninjatrader.queue.guice.NtTaskModule;
import com.bn.ninjatrader.service.exception.JsonParseExceptionMapper;
import com.bn.ninjatrader.service.filter.AuthorizationFilter;
import com.bn.ninjatrader.service.filter.CrossOriginResourceResponseFilter;
import com.bn.ninjatrader.service.filter.EventDispatchFilter;
import com.bn.ninjatrader.service.guice.NtServiceCacheModule;
import com.bn.ninjatrader.service.guice.NtServiceEventModule;
import com.bn.ninjatrader.service.guice.NtServiceSimulationModule;
import com.bn.ninjatrader.service.guice.NtServiceWorkerModule;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.bn.ninjatrader.service.provider.ObjectMapperContextResolver;
import com.bn.ninjatrader.service.resource.AlgorithmResource;
import com.bn.ninjatrader.service.resource.MigrateResource;
import com.bn.ninjatrader.service.resource.PriceResource;
import com.bn.ninjatrader.service.resource.ScanResource;
import com.bn.ninjatrader.service.resource.TestEventResource;
import com.bn.ninjatrader.service.resource.UserResource;
import com.bn.ninjatrader.service.task.HandleEventTask;
import com.bn.ninjatrader.service.task.ImportCSVPriceTask;
import com.bn.ninjatrader.service.task.ImportPSETraderDailyQuotesTask;
import com.bn.ninjatrader.service.task.PriceAdjustmentTask;
import com.bn.ninjatrader.service.task.RenameStockSymbolTask;
import com.bn.ninjatrader.service.task.RunSimulationTask;
import com.bn.ninjatrader.service.task.RunStockScannerTask;
import com.bn.ninjatrader.worker.AppEngineWorkerDispatcher;
import com.bn.ninjatrader.worker.WorkerDispatcher;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.netflix.archaius.guice.ArchaiusModule;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public class ServiceResourceConfig extends ResourceConfig {

  public ServiceResourceConfig() {
    final Injector injector = Guice.createInjector(
        new ArchaiusModule() {
          @Override
          protected void configureArchaius() {
          bindCascadeStrategy().to(AppEngineCascadeStrategy.class);
          }
        },
        new NtAuthModule(),
        new NtTaskModule() {
          @Override
          protected void bindTaskDispatcher() {
            bind(TaskDispatcher.class).to(AppEngineTaskDispatcher.class);
          }
        },
        new NtMessagingModule(),
        new NtServiceCacheModule() {
          @Override
          protected void bindCache() {
            bind(CacheClient.class).to(AppEngineCacheClient.class);
          }

          @Override
          public List<DailyQuote> provideListCachedDailyQuotes(CacheClient cacheClient) {
            return cacheClient.getList(DAILY_QUOTES_NAMESPACE);
          }
        },
        new NtServiceEventModule() {
          @Override
          protected void bindMessagePublisher() {
          bind(MessagePublisher.class).to(AppEngineMessagePublisher.class);
          }
        },
        new NtServiceSimulationModule(),
        new NtServiceWorkerModule() {
          @Override
          protected void bindWorkerDispatcher() {
          bind(WorkerDispatcher.class).to(AppEngineWorkerDispatcher.class);
          }
        },
        new NtModelDatastoreModule(),
        new NtClockModule()
    );

    //Provideers and Mappers
    packages(JsonParseExceptionMapper.class.getPackage().getName());

    // Resources
    register(injector.getInstance(AlgorithmResource.class));
    register(injector.getInstance(MigrateResource.class));
    register(injector.getInstance(PriceResource.class));
    register(injector.getInstance(ScanResource.class));
    register(injector.getInstance(UserResource.class));
    register(injector.getInstance(TestEventResource.class));//todo remove

    //Filters
    register(injector.getInstance(CrossOriginResourceResponseFilter.class));
    register(injector.getInstance(AuthorizationFilter.class));
    register(injector.getInstance(EventDispatchFilter.class));

    //Tasks
    register(injector.getInstance(HandleEventTask.class));
    register(injector.getInstance(RunSimulationTask.class));
    register(injector.getInstance(RunStockScannerTask.class));
    register(injector.getInstance(ImportPSETraderDailyQuotesTask.class));
    register(injector.getInstance(PriceAdjustmentTask.class));
    register(injector.getInstance(RenameStockSymbolTask.class));
    register(injector.getInstance(ImportPSETraderDailyQuotesTask.class));
    register(injector.getInstance(ImportCSVPriceTask.class));

    //Appengine Cron
    register(injector.getInstance(ImportQuotesCronJob.class));

    // Mappers, Provicers, Resolvers etc.
    register(injector.getInstance(ObjectMapperContextResolver.class));
    register(injector.getInstance(LocalDateParamConverterProvider.class));

    // Features
    register(SseFeature.class);
  }
}
