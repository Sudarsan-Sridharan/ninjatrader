package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.data.history.PriceImporter;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.servlets.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

/**
 * To execute multiple processes:
 * curl -X POST localhost:8081/tasks/import-price"
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Timed
public class ImportCSVPriceTask extends Task {

  private static final Logger LOG = LoggerFactory.getLogger(ImportCSVPriceTask.class);

  private final PriceImporter priceImporter;

  @Inject
  public ImportCSVPriceTask(PriceImporter priceImporter) {
    super("import-price");
    this.priceImporter = priceImporter;
  }

  @Override
  public void execute(ImmutableMultimap<String, String> args, PrintWriter printWriter) throws Exception {
    LOG.info("Executing ImportPrice {}", args);
    priceImporter.importPrices();
  }
}
