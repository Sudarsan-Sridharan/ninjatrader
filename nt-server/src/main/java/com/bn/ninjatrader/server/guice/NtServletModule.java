package com.bn.ninjatrader.server.guice;

import com.bn.ninjatrader.server.page.ChartPage;
import com.google.sitebricks.SitebricksModule;
import com.google.sitebricks.SitebricksServletModule;

/**
 * Created by Brad on 4/26/16.
 */
public class NtServletModule extends SitebricksModule {

  @Override
  protected SitebricksServletModule servletModule() {
    return new SitebricksServletModule() {
      @Override
      protected void configureCustomServlets() {
        serve("/").with(ChartPage.class);
      }
    };
  }
}
