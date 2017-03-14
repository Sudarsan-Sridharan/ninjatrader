package com.bn.ninjatrader.server.page;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.request.FindTradeAlgorithmRequest;
import com.bn.ninjatrader.server.util.HtmlWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Brad on 4/26/16.
 */
@Singleton
@Path("/")
public class DashboardPage {
  private static final Logger LOG = LoggerFactory.getLogger(DashboardPage.class);
  private static final String TEMPLATE_PATH = "velocity/pages/dashboard.vm";

  private final TradeAlgorithmDao tradeAlgorithmDao;
  private final HtmlWriterFactory htmlWriterFactory;

  @Inject
  public DashboardPage(final TradeAlgorithmDao tradeAlgorithmDao,
                       final HtmlWriterFactory htmlWriterFactory) {
    this.tradeAlgorithmDao = tradeAlgorithmDao;
    this.htmlWriterFactory = htmlWriterFactory;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String showDashboard() {
    final List<TradeAlgorithm> algos = tradeAlgorithmDao.find(FindTradeAlgorithmRequest.withUserId("ADMIN"));

    return htmlWriterFactory.createWithTemplate(TEMPLATE_PATH)
        .put("tradeAlgorithms", algos)
        .write();
  }
}
