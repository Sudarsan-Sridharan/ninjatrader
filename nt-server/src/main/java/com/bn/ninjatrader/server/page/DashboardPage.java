package com.bn.ninjatrader.server.page;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.request.FindTradeAlgorithmRequest;
import com.bn.ninjatrader.server.util.HtmlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by Brad on 4/26/16.
 */
@Singleton
@Path("/")
public class DashboardPage {
  private static final Logger LOG = LoggerFactory.getLogger(DashboardPage.class);

  private final TradeAlgorithmDao tradeAlgorithmDao;

  @Inject
  public DashboardPage(final TradeAlgorithmDao tradeAlgorithmDao) {
    this.tradeAlgorithmDao = tradeAlgorithmDao;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String showDashboard(@Context final HttpServletRequest req)
      throws ServletException, IOException {

    final List<TradeAlgorithm> algos = tradeAlgorithmDao.find(FindTradeAlgorithmRequest.withUserId("ADMIN"));

    return HtmlWriter.withTemplatePath("velocity/pages/dashboard.vm")
        .put("tradeAlgorithms", algos)
        .put("contextPath", req.getContextPath())
        .put("serviceHost", "http://localhost:8080")
        .write();
  }
}
