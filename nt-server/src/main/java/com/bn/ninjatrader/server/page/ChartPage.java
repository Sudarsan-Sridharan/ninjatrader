package com.bn.ninjatrader.server.page;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
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
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Brad on 4/26/16.
 */
@Singleton
@Path("/")
public class ChartPage {
  private static final Logger LOG = LoggerFactory.getLogger(ChartPage.class);

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String showChart(@Context final HttpServletRequest req)
      throws ServletException, IOException {
    final Writer writer = new StringWriter();
    final VelocityContext context = new VelocityContext();
    context.put("contextPath", req.getContextPath());
    Velocity.mergeTemplate("velocity/pages/chart.vm", "UTF-8", context, writer);
    return writer.toString();
  }
}
