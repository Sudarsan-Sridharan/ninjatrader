//package com.bn.ninjatrader.server;
//
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.webapp.WebAppContext;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.net.URI;
//import java.net.URL;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class ServerApplication {
//  private static final Logger LOG = LoggerFactory.getLogger(ServerApplication.class);
//
//  public static void main(String[] args) throws Exception {
//    final ClassLoader cl = ServerApplication.class.getClassLoader();
//    final URL f = cl.getResource("application.properties");
//    if (f == null) {
//      throw new RuntimeException("Unable to find resource directory");
//    }
//
//    final URI webRootUri = f.toURI().resolve("./").normalize();
//    System.err.println("WebRoot is " + webRootUri);
//
//    File webXml = new File(webRootUri.toString() + "/WEB-INF/web.xml");
//    assert webXml.isFile();
//
//    LOG.info("WEB.xml: {}", webXml.getAbsoluteFile());
//
//    Server server = new Server(9090);
//
//    WebAppContext webAppContext = new WebAppContext();
//    webAppContext.setDescriptor(webRootUri.toString() + "/WEB-INF/web.xml");
//    webAppContext.setContextPath("/");
//    webAppContext.setResourceBase(webRootUri.toString());
//
//    server.setHandler(webAppContext);
//
//    try {
//      server.start();
//      server.join();
//    } finally {
//      server.destroy();
//    }
//  }
//}
