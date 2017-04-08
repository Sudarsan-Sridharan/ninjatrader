package com.bn.ninjatrader.server.page;

import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.server.util.HtmlWriterFactory;
import com.google.common.collect.Lists;
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
  private static final String TEMPLATE_PATH = "/velocity/pages/dashboard.vm";

  private final HtmlWriterFactory htmlWriterFactory;

  @Inject
  public DashboardPage(final HtmlWriterFactory htmlWriterFactory) {
    this.htmlWriterFactory = htmlWriterFactory;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String showDashboard() {
    // TODO use ajax to get algos
    final List<TradeAlgorithm> algos = Lists.newArrayList(
        TradeAlgorithm.builder().userId("ADMIN").id("ADMIN").description("Secret Sauce").algorithm("").build(),
        TradeAlgorithm.builder().userId("ADMIN").id("RISKON").description("10% Risk On").algorithm("").build()
    );

    return htmlWriterFactory.createWithTemplate(TEMPLATE_PATH)
        .put("tradeAlgorithms", algos)
        .write();
  }
}
