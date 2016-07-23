package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.data.IchimokuData;
import org.jongo.MongoCollection;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 5/4/16.
 */
public class IchimokuDaoTest extends AbstractDaoTest {

  private IchimokuDao ichimokuDao;

  @BeforeClass
  public void setup() {
    ichimokuDao = injector.getInstance(IchimokuDao.class);
  }

  @BeforeMethod
  @AfterMethod
  public void cleanup() {
    MongoCollection collection = ichimokuDao.getMongoCollection();
    collection.remove();
  }

  @Test
  public void testSaveAndFind() {
    IchimokuData data = new IchimokuData("MEG", 2016);
    Ichimoku ichimoku = new Ichimoku(LocalDate.now(), 1d, 1d, 1d, 1d, 1d);
    data.getData().add(ichimoku);

    ichimokuDao.save(data);

    Optional<IchimokuData> foundData = ichimokuDao.findPriceByYear("MEG", 2016);
    assertTrue(foundData.isPresent());

    IchimokuData result = foundData.get();
    assertEquals(result.getSymbol(), "MEG");
    assertEquals(result.getYear(), 2016);
    assertEquals(result.getData().size(), 1);

    Ichimoku ichimokuResult = result.getData().get(0);
    TestUtil.assertIchimokuEquals(ichimokuResult, ichimoku);
  }

  @Test
  public void testUpsert() {
    Ichimoku ichimoku1 = new Ichimoku(LocalDate.now(), 1d, 1d, 1d, 1d, 1d);
    Ichimoku ichimoku2 = new Ichimoku(LocalDate.now(), 2d, 2d, 2d, 2d, 2d);

    IchimokuData data = new IchimokuData("MEG", 2016);
    data.getData().add(ichimoku1);

    ichimokuDao.save(data);

    ichimokuDao.upsert("MEG", ichimoku2);



  }
}
