//package com.beachninja.ninjatrader.service.appengine.standard.guice;
//
//import com.beachninja.ninjatrader.service.appengine.standard.config.ServiceResourceConfig2;
//import com.bn.ninjatrader.common.guice.NtClockModule;
//import com.bn.ninjatrader.model.datastore.guice.NtModelDatastoreModule;
//import com.bn.ninjatrader.service.guice.NtServiceModule;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import com.google.inject.Singleton;
//import com.google.inject.servlet.GuiceServletContextListener;
//import com.google.inject.servlet.ServletModule;
//import org.glassfish.jersey.servlet.ServletContainer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.inject.Inject;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class GuiceServletConfig extends GuiceServletContextListener {
//
//  private Lifecycle lifecycle;
//
//  @Override
//  protected Injector getInjector() {
//    final Injector injector = Guice.createInjector(
//        new NtServiceModule(),
//        new NtModelDatastoreModule(),
//        new NtClockModule(),
//        new ServletModule() {
//          @Override
//          protected void configureServlets() {
//            serve("/service/*").with(JerseyContainer.class);
//            serve("/hello").with(new HttpServlet() {
//              @Override
//              protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//                resp.getWriter().append("Hello World! :)").flush();
//              }
//            });
//          }
//        }
//    );
//    return injector;
//  }
//
//  @Override
//  public void contextInitialized(ServletContextEvent servletContextEvent) {
//    super.contextInitialized(servletContextEvent);
//    final Injector injector = (Injector) servletContextEvent.getServletContext()
//        .getAttribute(Injector.class.getName());
//
//    System.setProperty("hazelcast.phone.home.enabled", "false");
//
//    lifecycle = injector.getInstance(Lifecycle.class);
//    lifecycle.start();
//  }
//
//  @Override
//  public void contextDestroyed(ServletContextEvent servletContextEvent) {
//    super.contextDestroyed(servletContextEvent);
//    if (lifecycle != null) {
//      lifecycle.stop();
//    }
//  }
//
//  /**
//   * Jersey container
//   */
//  @Singleton
//  private static final class JerseyContainer extends ServletContainer {
//    private static final Logger LOG = LoggerFactory.getLogger(JerseyContainer.class);
//
//    @Inject
//    public JerseyContainer(final ServiceResourceConfig2 resourceConfig) {
//      super(resourceConfig);
//
//      LOG.info("Started Jersey Container");
//    }
//  }
//}
