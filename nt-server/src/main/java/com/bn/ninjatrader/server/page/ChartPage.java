package com.bn.ninjatrader.server.page;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Brad on 4/26/16.
 */
@Singleton
public class ChartPage extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(ChartPage.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    VelocityContext context = new VelocityContext();
    context.put("contextPath", req.getContextPath());

    Velocity.mergeTemplate("velocity/pages/chart.vm", "UTF-8", context, resp.getWriter());
  }
}
