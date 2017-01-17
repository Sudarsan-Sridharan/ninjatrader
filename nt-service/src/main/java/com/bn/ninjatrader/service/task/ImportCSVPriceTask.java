package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.history.CsvPriceImporter;
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

  private final CsvPriceImporter csvPriceImporter;

  @Inject
  public ImportCSVPriceTask(CsvPriceImporter csvPriceImporter) {
    super("import-csv-price");
    this.csvPriceImporter = csvPriceImporter;
  }

  @Override
  public void execute(ImmutableMultimap<String, String> args, PrintWriter printWriter) throws Exception {
    LOG.info("Executing ImportPrice {}", args);
    csvPriceImporter.importPrices();
  }
}
