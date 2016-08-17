package com.bn.ninjatrader.server.listener;

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
public class InitializeListener implements ServletContextListener {

  private static final Logger log = LoggerFactory.getLogger(InitializeListener.class);

  public void contextInitialized(ServletContextEvent event) {
    Properties prop = new Properties();
    InputStream is = InitializeListener.class.getResourceAsStream("/velocity.properties");
    try {
      prop.load(is);

      String logFile = event.getServletContext().getRealPath("/WEB-INF") + "/velocity.transaction";
      log.info("logFile=" + logFile);
      prop.setProperty("runtime.log", logFile);

      log.info("Loaded velocity properties: " + prop);

      Velocity.init(prop);
      log.info("Initialized singleton velocity instance");

    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  public void contextDestroyed(ServletContextEvent sce) {

  }
}
