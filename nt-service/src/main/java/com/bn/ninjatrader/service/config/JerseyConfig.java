package com.bn.ninjatrader.service.config;

import com.bn.ninjatrader.dataimport.guice.NtDataModule;
import com.bn.ninjatrader.model.guice.NtModelMongoModule;
import com.bn.ninjatrader.process.guice.NtProcessModule;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.bn.ninjatrader.service.provider.ObjectMapperContextResolver;
import com.bn.ninjatrader.service.resource.PriceResource;
import com.bn.ninjatrader.service.resource.SimulationResource;
import com.bn.ninjatrader.service.task.ImportCSVPriceTask;
import com.bn.ninjatrader.service.task.ImportPSETraderDailyQuotesTask;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author bradwee2000@gmail.com
 */
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    final Injector injector = Guice.createInjector(
        new NtModelMongoModule(),
        new NtDataModule(),
        new NtProcessModule()
    );

//    register(injector.getInstance(EMAResource.class));
//    register(injector.getInstance(IchimokuResource.class));
//    register(injector.getInstance(MeanResource.class));
    register(injector.getInstance(PriceResource.class));
//    register(injector.getInstance(RSIResource.class));
    register(injector.getInstance(SimulationResource.class));
//    register(injector.getInstance(SMAResource.class));

//    register(injector.getInstance(CalcTask.class));
    register(injector.getInstance(ImportPSETraderDailyQuotesTask.class));
    register(injector.getInstance(ImportCSVPriceTask.class));

    // Context Resolver
    register(injector.getInstance(ObjectMapperContextResolver.class));

    // Converter providers
    register(LocalDateParamConverterProvider.class);

    // Migration
//    register(injector.getInstance(PriceResource2.class));
  }
}
