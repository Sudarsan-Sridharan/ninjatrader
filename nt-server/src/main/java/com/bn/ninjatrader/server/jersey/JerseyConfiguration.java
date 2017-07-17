package com.bn.ninjatrader.server.jersey;

import com.bn.ninjatrader.server.guice.NtArchaiusModule;
import com.bn.ninjatrader.server.page.AlgorithmPage;
import com.bn.ninjatrader.server.page.ChartPage;
import com.bn.ninjatrader.server.page.DashboardPage;
import com.bn.ninjatrader.server.page.ScannerPage;
import com.bn.ninjatrader.server.page.TokenPage;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author bradwee2000@gmail.com
 */
public class JerseyConfiguration extends ResourceConfig {

  public JerseyConfiguration() {
    final Injector injector = Guice.createInjector(
        new NtArchaiusModule());
    register(injector.getInstance(AlgorithmPage.class));
    register(injector.getInstance(ChartPage.class));
    register(injector.getInstance(DashboardPage.class));
    register(injector.getInstance(ScannerPage.class));
    register(injector.getInstance(TokenPage.class));
  }
}
