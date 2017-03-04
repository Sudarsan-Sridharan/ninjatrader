package com.bn.ninjatrader.server.page;

import com.bn.ninjatrader.server.util.HtmlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by Brad on 4/26/16.
 */
@Singleton
@Path("/scanner")
public class ScannerPage {
  private static final Logger LOG = LoggerFactory.getLogger(ScannerPage.class);

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String showScanResults(@Context final HttpServletRequest req)
      throws ServletException, IOException {

    return HtmlWriter.withTemplatePath("velocity/pages/scanner.vm")
        .put("contextPath", req.getContextPath())
        .put("serviceHost", "http://localhost:8080")
        .write();
  }
}
