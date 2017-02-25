package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.model.deprecated.Stock;
import com.bn.ninjatrader.model.mongo.guice.NtModelTestModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Brad on 5/4/16.
 */
public class StockDaoTest {

  private static Injector injector;
  private StockDao stockDao;

  @BeforeClass
  public static void setup() {
    injector = Guice.createInjector(new NtModelTestModule());
  }

  @Before
  public void before() {
    stockDao = injector.getInstance(StockDao.class);
    stockDao.getMongoCollection().remove();
  }

  @Test
  public void testSaveAndFind() {
    // Prepare document
    Stock stock = new Stock("MEG", "Megaworld");

    // Save boardlot document
    stockDao.save(stock);

    // Find document
    List<Stock> result = stockDao.find();
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getSymbol()).isEqualTo(stock.getSymbol());
    assertThat(result.get(0).getName()).isEqualTo(stock.getName());

    // Add another boardlot
    stock = new Stock("BDO", "Banco De Oro");
    stockDao.save(stock);

    result = stockDao.find();
    assertThat(result).hasSize(2);
    assertThat(result.get(1).getSymbol()).isEqualTo(stock.getSymbol());
    assertThat(result.get(1).getName()).isEqualTo(stock.getName());
  }

  @Test
  public void testInsertAndUpdate() {
    // Prepare document
    Stock stock = new Stock("MEG", "Megaworld");

    // Save boardlot document
    stockDao.save(stock);

    // Update w/ Security REPORT_ID
    stock.setSecurityId(300);
    stockDao.save(stock);

    Optional<Stock> foundStock = stockDao.findBySymbol("MEG");

    assertThat(foundStock.isPresent()).isTrue();
    assertThat(foundStock.get().getSymbol()).isEqualTo(stock.getSymbol());
    assertThat(foundStock.get().getSecurityId()).isEqualTo(300);
  }

  @Test
  public void testFindBySymbol() {
    Stock stock1 = new Stock("MEG", "Megaworld");
    Stock stock2 = new Stock("BDO", "Banco de Oro");

    stockDao.save(stock1);
    stockDao.save(stock2);

    // Verify boardlot 1
    Optional<Stock> foundStock = stockDao.findBySymbol(stock1.getSymbol());
    assertThat(foundStock.isPresent());
    assertThat(foundStock.get().getSymbol()).isEqualTo(stock1.getSymbol());
    assertThat(foundStock.get().getName()).isEqualTo(stock1.getName());

    // Verify boardlot 2
    foundStock = stockDao.findBySymbol(stock2.getSymbol());
    assertThat(foundStock.isPresent()).isTrue();
    assertThat(foundStock.get().getSymbol()).isEqualTo(stock2.getSymbol());
    assertThat(foundStock.get().getName()).isEqualTo(stock2.getName());
  }

  @Test
  public void testFindWithNoSecurityId() {
    Stock stock1 = new Stock("MEG", "Megaworld");
    Stock stock2 = new Stock("BDO", "Banco de Oro", 200);

    stockDao.save(stock1);
    stockDao.save(stock2);

    List<Stock> stockList = stockDao.findWithNoSecurityId();

    assertThat(stockList).hasSize(1);
    assertThat(stockList.get(0).getSymbol()).isEqualTo(stock1.getSymbol());
  }

  @Test
  public void testSaveSymbolAndName() {
    Stock stock = new Stock("MEG", "Wrong Name", 200);

    stockDao.save(stock);

    stock.setName("Megaworld");

    stockDao.saveSymbolAndName(stock);

    Optional<Stock> foundStock = stockDao.findBySymbol("MEG");

    assertThat(foundStock.isPresent()).isTrue();
    assertThat(foundStock.get().getSymbol()).isEqualTo("MEG");
    assertThat(foundStock.get().getName()).isEqualTo("Megaworld");
    assertThat(foundStock.get().getSecurityId()).isEqualTo(200);
  }

  @Test
  public void testGetAllSymbols() {
    Stock stock1 = new Stock("MEG", "Megaworld");
    Stock stock2 = new Stock("BDO", "Banco de Oro");
    Stock stock3 = new Stock("MBT", "Metrobank");
    Stock stock4 = new Stock("FLI", "Filinvest");

    stockDao.save(stock1);
    stockDao.save(stock2);
    stockDao.save(stock3);
    stockDao.save(stock4);

    final List<String> symbols = stockDao.getAllSymbols();
    assertThat(symbols).containsExactlyInAnyOrder("MEG", "BDO", "MBT", "FLI");
  }
}
