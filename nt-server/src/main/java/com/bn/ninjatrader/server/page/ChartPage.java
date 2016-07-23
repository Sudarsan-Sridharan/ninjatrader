package com.bn.ninjatrader.server.page;

import com.bn.ninjatrader.data.history.FileDataImporter;
import com.bn.ninjatrader.server.service.ProcessService;
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
import java.time.LocalDate;

/**
 * Created by Brad on 4/26/16.
 */
@Singleton
public class ChartPage extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(ChartPage.class);

  @Inject
  private FileDataImporter dataImporter;

  @Inject
  private ProcessService processService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    VelocityContext context = new VelocityContext();

    context.put("contextPath", req.getContextPath());

    Velocity.mergeTemplate("velocity/pages/chart.vm", "UTF-8", context, resp.getWriter());

    String action = req.getParameter("action");
    if ("importData".equals(action)) {
      try {
        dataImporter.importData();
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    } else if ("calcAll".equals(action)) {
      processService.processAll(LocalDate.now().minusYears(100), LocalDate.now());
    } else if ("calcPartial".equals(action)) {
      processService.processAll(LocalDate.now().minusMonths(8), LocalDate.now());
    }
  }
}
