package com.bn.ninjatrader.server.page;

import com.bn.ninjatrader.server.util.HtmlWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Brad on 4/26/16.
 */
@Singleton
@Path("/chart")
public class ChartPage {
  private static final Logger LOG = LoggerFactory.getLogger(ChartPage.class);
  private static final String TEMPLATE_PATH = "/velocity/pages/chart.vm";

  private final HtmlWriterFactory htmlWriterFactory;

  @Inject
  public ChartPage(final HtmlWriterFactory htmlWriterFactory) {
    this.htmlWriterFactory = htmlWriterFactory;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String showChart() {
    return htmlWriterFactory.createWithTemplate(TEMPLATE_PATH).write();
  }
}
