package com.beachninja.ninjatrader.service.appengine.standard.config;

import com.bn.ninjatrader.dataimport.guice.NtDataModule;
import com.bn.ninjatrader.model.datastore.guice.NtModelDatastoreModule;
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
        new NtModelDatastoreModule(),
        new NtDataModule()
    );

    register(injector.getInstance(PriceResource.class));
    register(injector.getInstance(SimulationResource.class));

    register(injector.getInstance(ImportPSETraderDailyQuotesTask.class));
    register(injector.getInstance(ImportCSVPriceTask.class));

    // Context Resolver
    register(injector.getInstance(ObjectMapperContextResolver.class));

    // Converter providers
    register(LocalDateParamConverterProvider.class);
  }
}
