package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.history.CsvPriceImporter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * To execute multiple processes:
 * curl -X POST localhost:8081/tasks/import-csv-price
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/tasks/importcsvprice")
public class ImportCSVPriceTask {

  private static final Logger LOG = LoggerFactory.getLogger(ImportCSVPriceTask.class);

  private final CsvPriceImporter csvPriceImporter;

  @Inject
  public ImportCSVPriceTask(final CsvPriceImporter csvPriceImporter) {
    this.csvPriceImporter = csvPriceImporter;
  }

  @POST
  public void importCsvPrices() throws Exception {
    csvPriceImporter.importPrices();
  }
}
