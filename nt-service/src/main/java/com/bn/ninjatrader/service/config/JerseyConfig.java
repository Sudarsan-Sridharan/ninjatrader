package com.bn.ninjatrader.service.config;

import com.bn.ninjatrader.dataimport.guice.NtDataModule;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.process.guice.NtProcessModule;
import com.bn.ninjatrader.service.migrate.PriceResource2;
import com.bn.ninjatrader.service.provider.ObjectMapperProvider;
import com.bn.ninjatrader.service.resource.*;
import com.bn.ninjatrader.service.task.CalcTask;
import com.bn.ninjatrader.service.task.ImportCSVPriceTask;
import com.bn.ninjatrader.service.task.ImportPSETraderDailyQuotesTask;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.jersey.server.ResourceConfig;

import java.time.Clock;
import java.time.ZoneId;

/**
 * @author bradwee2000@gmail.com
 */
public class JerseyConfig extends ResourceConfig {
  public static final String PH_ZONE_ID = "Asia/Manila";

  public JerseyConfig() {
    final Injector injector = Guice.createInjector(
        new NtModelModule(),
        new NtDataModule(),
        new NtProcessModule(),
        new AbstractModule() {
          @Override
          protected void configure() {
            bind(Clock.class).toInstance(Clock.system(ZoneId.of(PH_ZONE_ID)));
          }
        });

    register(injector.getInstance(EMAResource.class));
    register(injector.getInstance(IchimokuResource.class));
    register(injector.getInstance(MeanResource.class));
    register(injector.getInstance(PriceResource.class));
    register(injector.getInstance(RSIResource.class));
    register(injector.getInstance(SimulationResource.class));
    register(injector.getInstance(SMAResource.class));

    register(injector.getInstance(CalcTask.class));
    register(injector.getInstance(ImportPSETraderDailyQuotesTask.class));
    register(injector.getInstance(ImportCSVPriceTask.class));

    register(injector.getInstance(ObjectMapperProvider.class));


    // Migration
    register(injector.getInstance(PriceResource2.class));
  }
}
