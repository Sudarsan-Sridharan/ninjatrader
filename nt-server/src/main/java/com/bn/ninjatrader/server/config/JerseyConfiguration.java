package com.bn.ninjatrader.server.config;

import com.bn.ninjatrader.server.page.ChartPage;
import com.bn.ninjatrader.server.page.DashboardPage;
import com.bn.ninjatrader.server.page.ScannerPage;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author bradwee2000@gmail.com
 */
public class JerseyConfiguration extends ResourceConfig {

  public JerseyConfiguration() {
    final Injector injector = Guice.createInjector();
    register(injector.getInstance(ChartPage.class));
    register(injector.getInstance(DashboardPage.class));
    register(injector.getInstance(ScannerPage.class));
  }
}
