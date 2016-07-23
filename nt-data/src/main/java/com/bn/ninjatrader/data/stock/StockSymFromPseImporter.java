package com.bn.ninjatrader.data.stock;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.model.dao.StockDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.thirdparty.pse.PseService;
import com.bn.ninjatrader.thirdparty.pse.PseStock;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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
    List<PseStock> pseStocks = pseService.getAllStockIndices();
    for (PseStock pseStock : pseStocks) {
      Stock stock = pseStock.toStock();
      log.debug("Saving stock from PSE: {}", stock);
      stockDao.saveSymbolAndName(stock);
    }
    importSecurityId();
  }

  public void importSecurityId() throws IOException {
    for (Stock stock : stockDao.findWithNoSecurityId()) {
      try {
        log.debug("Getting Security ID for Stock: {}", stock.getSymbol());
        int securityId = pseService.getSecurityId(stock.getSymbol());
        stock.setSecurityId(securityId);
        stockDao.save(stock);
      } catch (Exception e) {
        log.error("Error processing stock: " + stock.getSymbol(), e);
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
