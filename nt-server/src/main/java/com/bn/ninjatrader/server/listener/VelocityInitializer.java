package com.bn.ninjatrader.server.listener;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Brad
 */
public class VelocityInitializer implements ServletContextListener {
  private static final Logger log = LoggerFactory.getLogger(VelocityInitializer.class);

  public void contextInitialized(ServletContextEvent event) {
    final Properties prop = new Properties();
    final InputStream is = VelocityInitializer.class.getResourceAsStream("/velocity.properties");
    try {
      prop.load(is);
      Velocity.init(prop);
      log.info("Initialized singleton velocity instance");
    } catch (final IOException e) {
      log.error(e.getMessage(), e);
    } finally {
      IOUtils.closeQuietly(is);
    }
  }

  public void contextDestroyed(ServletContextEvent sce) {

  }
}
