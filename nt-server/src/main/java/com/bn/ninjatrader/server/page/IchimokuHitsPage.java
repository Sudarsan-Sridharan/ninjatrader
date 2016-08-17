package com.bn.ninjatrader.server.page;

import com.bn.ninjatrader.service.calc.CalcProcessService;
import com.bn.ninjatrader.service.indicator.IchimokuService;
import com.google.inject.Inject;
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
public class IchimokuHitsPage extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(IchimokuHitsPage.class);

  @Inject
  private IchimokuService ichimokuService;

  @Inject
  private CalcProcessService processService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    VelocityContext context = new VelocityContext();
    context.put("contextPath", req.getContextPath());

    Velocity.mergeTemplate("velocity/pages/ichimokuhits.vm", "UTF-8", context, resp.getWriter());
  }
}
