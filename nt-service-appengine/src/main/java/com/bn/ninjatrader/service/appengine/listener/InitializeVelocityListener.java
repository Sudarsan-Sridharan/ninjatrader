package com.bn.ninjatrader.service.appengine.listener;

import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Brad
 */
@WebListener
public class InitializeVelocityListener implements ServletContextListener {

  private static final Logger log = LoggerFactory.getLogger(InitializeVelocityListener.class);

  public void contextInitialized(final ServletContextEvent event) {
    final Properties prop = new Properties();
    final InputStream is = InitializeVelocityListener.class.getResourceAsStream("/velocity.properties");
    try {
      prop.load(is);
      Velocity.init(prop);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  public void contextDestroyed(final ServletContextEvent sce) {

  }
}
