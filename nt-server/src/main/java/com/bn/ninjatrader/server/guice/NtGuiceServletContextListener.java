package com.bn.ninjatrader.server.guice;

import com.bn.ninjatrader.dataimport.guice.NtDataModule;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.process.guice.NtProcessModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

/**
 * Created by Brad on 4/26/16.
 */
public class NtGuiceServletContextListener extends GuiceServletContextListener {

  private static final Logger log = LoggerFactory.getLogger(NtGuiceServletContextListener.class);

  @Override
  protected Injector getInjector() {
    Injector injector = Guice.createInjector(
            new NtModelModule(),
            new NtServletModule(),
            new NtDataModule(),
            new NtProcessModule()
    );
    return injector;
  }

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    super.contextInitialized(servletContextEvent);
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    super.contextDestroyed(servletContextEvent);
  }
}
