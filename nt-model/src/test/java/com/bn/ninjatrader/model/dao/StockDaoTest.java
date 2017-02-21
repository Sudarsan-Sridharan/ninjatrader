package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.model.guice.NtModelTestModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jongo.MongoCollection;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

/**
 * Created by Brad on 5/4/16.
 */
public class StockDaoTest {

  private StockDao stockDao;

  @BeforeClass
  public void setup() {
    Injector injector = Guice.createInjector(new NtModelTestModule());
    stockDao = injector.getInstance(StockDao.class);
  }

  @BeforeMethod
  public void cleanup() {
    MongoCollection collection = stockDao.getMongoCollection();
    collection.remove();
  }

  @Test
  public void testSaveAndFind() {
    // Prepare document
    Stock stock = new Stock("MEG", "Megaworld");

    // Save boardlot document
    stockDao.save(stock);

    // Find document
    List<Stock> result = stockDao.find();
    assertNotNull(result);
    assertEquals(result.size(), 1);
    assertEquals(result.get(0).getSymbol(), stock.getSymbol());
    assertEquals(result.get(0).getName(), stock.getName());

    // Add another boardlot
    stock = new Stock("BDO", "Banco De Oro");
    stockDao.save(stock);

    result = stockDao.find();
    assertEquals(result.size(), 2);
    assertEquals(result.get(1).getSymbol(), stock.getSymbol());
    assertEquals(result.get(1).getName(), stock.getName());
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

    assertTrue(foundStock.isPresent());
    assertEquals(foundStock.get().getSymbol(), stock.getSymbol());
    assertEquals(foundStock.get().getSecurityId(), 300);
  }

  @Test
  public void testFindBySymbol() {
    Stock stock1 = new Stock("MEG", "Megaworld");
    Stock stock2 = new Stock("BDO", "Banco de Oro");

    stockDao.save(stock1);
    stockDao.save(stock2);

    // Verify boardlot 1
    Optional<Stock> foundStock = stockDao.findBySymbol(stock1.getSymbol());
    assertTrue(foundStock.isPresent());
    assertEquals(foundStock.get().getSymbol(), stock1.getSymbol());
    assertEquals(foundStock.get().getName(), stock1.getName());

    // Verify boardlot 2
    foundStock = stockDao.findBySymbol(stock2.getSymbol());
    assertTrue(foundStock.isPresent());
    assertEquals(foundStock.get().getSymbol(), stock2.getSymbol());
    assertEquals(foundStock.get().getName(), stock2.getName());
  }

  @Test
  public void testFindWithNoSecurityId() {
    Stock stock1 = new Stock("MEG", "Megaworld");
    Stock stock2 = new Stock("BDO", "Banco de Oro", 200);

    stockDao.save(stock1);
    stockDao.save(stock2);

    List<Stock> stockList = stockDao.findWithNoSecurityId();

    assertEquals(stockList.size(), 1);
    assertEquals(stockList.get(0).getSymbol(), stock1.getSymbol());
  }

  @Test
  public void testSaveSymbolAndName() {
    Stock stock = new Stock("MEG", "Wrong Name", 200);

    stockDao.save(stock);

    stock.setName("Megaworld");

    stockDao.saveSymbolAndName(stock);

    Optional<Stock> foundStock = stockDao.findBySymbol("MEG");

    assertTrue(foundStock.isPresent());
    assertEquals(foundStock.get().getSymbol(), "MEG");
    assertEquals(foundStock.get().getName(), "Megaworld");
    assertEquals(foundStock.get().getSecurityId(), 200);
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

    List<String> symbols = stockDao.getAllSymbols();
    assertNotNull(symbols);
    assertEquals(symbols.size(), 4);
    assertTrue(symbols.contains(stock1.getSymbol()));
    assertTrue(symbols.contains(stock2.getSymbol()));
    assertTrue(symbols.contains(stock3.getSymbol()));
    assertTrue(symbols.contains(stock4.getSymbol()));
  }
}
