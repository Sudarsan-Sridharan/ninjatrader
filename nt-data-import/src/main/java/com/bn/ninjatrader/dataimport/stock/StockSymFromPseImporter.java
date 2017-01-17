package com.bn.ninjatrader.dataimport.stock;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.model.dao.StockDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.thirdparty.pse.PseAllStockUpdate;
import com.bn.ninjatrader.thirdparty.pse.PseService;
import com.bn.ninjatrader.thirdparty.pse.PseStock;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Brad on 6/11/16.
 */
@Singleton
public class StockSymFromPseImporter {
  private static final Logger log = LoggerFactory.getLogger(StockSymFromPseImporter.class);

  @Inject
  private StockDao stockDao;

  @Inject
  private PseService pseService;

  public void importData() throws IOException {
    PseAllStockUpdate pseAllStockUpdate = pseService.getAllStockIndices();
    for (PseStock pseStock : pseAllStockUpdate.getStocks()) {
      Stock stock = pseStock.toStock();
      log.debug("Saving boardlot from PSE: {}", stock);
      stockDao.saveSymbolAndName(stock);
    }
    importSecurityId();
  }

  public void importSecurityId() throws IOException {
    for (Stock stock : stockDao.findWithNoSecurityId()) {
      try {
        log.debug("Getting Security REPORT_ID for Stock: {}", stock.getSymbol());
        int securityId = pseService.getSecurityId(stock.getSymbol());
        stock.setSecurityId(securityId);
        stockDao.save(stock);
      } catch (Exception e) {
        log.error("Error processing boardlot: " + stock.getSymbol(), e);
      }
    }
  }

  public static void main(String args[]) throws Exception {
    Injector injector = Guice.createInjector(
        new NtModelModule()
    );

    StockSymFromPseImporter app = injector.getInstance(StockSymFromPseImporter.class);
    app.importData();
  }
}
