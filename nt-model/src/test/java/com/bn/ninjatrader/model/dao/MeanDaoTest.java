package com.bn.ninjatrader.model.dao;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.data.MeanData;
import org.jongo.MongoCollection;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;

/**
 * Created by Brad on 5/4/16.
 */
public class MeanDaoTest extends AbstractDaoTest {

  private MeanDao meanDao;

  @BeforeClass
  public void setup() {
    meanDao = injector.getInstance(MeanDao.class);
  }

  @BeforeMethod
  @AfterMethod
  public void cleanup() {
    MongoCollection collection = meanDao.getMongoCollection();
    collection.remove();
  }

  @Test
  public void testSaveAndFind() {
    MeanData data = new MeanData("MEG", 2016, 1);
    Value value = new Value(LocalDate.now(), 1d);
    data.getData().add(value);

    meanDao.save(data);

    Optional<MeanData> foundData = meanDao.findByPeriod("MEG", 2016, 1);
    assertTrue(foundData.isPresent());

    MeanData result = foundData.get();
    assertEquals(result.getSymbol(), "MEG");
    assertEquals(result.getYear(), 2016);
    assertEquals(result.getPeriod(), 1);
    assertEquals(result.getData().size(), 1);

    Value valueResult = result.getData().get(0);
    assertEquals(valueResult.getDate(), value.getDate());
    assertEquals(valueResult.getValue(), value.getValue());

    // Wrong Period
    foundData = meanDao.findByPeriod("MEG", 2016, 2);
    assertFalse(foundData.isPresent());

    // Wrong Year
    foundData = meanDao.findByPeriod("MEG", 2015, 1);
    assertFalse(foundData.isPresent());

    // Wrong Symbol
    foundData = meanDao.findByPeriod("BDO", 2016, 1);
    assertFalse(foundData.isPresent());
  }

  @Test
  public void testSaveMeanData() {
    LocalDate date1 = LocalDate.of(2016, 1, 1);
    LocalDate date2 = LocalDate.of(2016, 1, 2);
    LocalDate date3 = LocalDate.of(2016, 1, 3);

    MeanData data = new MeanData("X", 2016, 1);
    Value value1 = new Value(date1, 1d);
    Value value2 = new Value(date2, 2d);
    data.getData().add(value1);
    data.getData().add(value2);

    meanDao.save(data);

    Value value3 = new Value(date2, 3d);
    Value value4 = new Value(date3, 4d);
    meanDao.save("X", 1, Lists.newArrayList(value3, value4));

    Optional<MeanData> foundData = meanDao.findByPeriod("X", 2016, 1);

    MeanData result = foundData.get();
    assertEquals(result.getData().size(), 3);

    Value valueResult = result.getData().get(0);
    assertEquals(valueResult.getDate(), date1);
    assertEquals(valueResult.getValue(), value1.getValue());

    valueResult = result.getData().get(1);
    assertEquals(valueResult.getDate(), date2);
    assertEquals(valueResult.getValue(), value3.getValue());

    valueResult = result.getData().get(2);
    assertEquals(valueResult.getDate(), date3);
    assertEquals(valueResult.getValue(), value4.getValue());
  }

  @Test
  public void testSaveSorting() {
    LocalDate date1 = LocalDate.of(2016, 1, 1);
    LocalDate date2 = LocalDate.of(2016, 1, 2);
    LocalDate date3 = LocalDate.of(2016, 1, 3);

    MeanData data = new MeanData("X", 2016, 1);
    Value value1 = new Value(date1, 1d);
    Value value2 = new Value(date2, 2d);
    Value value3 = new Value(date3, 3d);

    data.getData().add(value3);
    data.getData().add(value1);
    data.getData().add(value2);

    // Save data
    meanDao.save(data);

    // Retrieve data
    Optional<MeanData> foundData = meanDao.findByPeriod("X", 2016, 1);

    // Verify size
    MeanData result = foundData.get();
    assertEquals(result.getData().size(), 3);

    // Verify order of data
    Value valueResult = result.getData().get(0);
    assertEquals(valueResult.getDate(), date1);

    valueResult = result.getData().get(1);
    assertEquals(valueResult.getDate(), date2);

    valueResult = result.getData().get(2);
    assertEquals(valueResult.getDate(), date3);
  }

  @Test
  public void testSaveValueWithMixedYear() {
    LocalDate date1 = LocalDate.of(2015, 1, 1);
    LocalDate date2 = LocalDate.of(2016, 1, 1);
    LocalDate date3 = LocalDate.of(2016, 1, 2);

    Value value1 = new Value(date1, 1d);
    Value value2 = new Value(date2, 2d);
    Value value3 = new Value(date3, 3d);

    List<Value> values = Lists.newArrayList();

    meanDao.save("MEG", 1, Lists.newArrayList(value3, value1, value2));

    // 2015
    Optional<MeanData> foundData = meanDao.findByPeriod("MEG", 2015, 1);
    assertTrue(foundData.isPresent());

    // Verify 2015 data
    MeanData meanData = foundData.get();
    assertEquals(meanData.getData().size(), 1);
    assertEquals(meanData.getData().get(0).getDate(), date1);
    assertEquals(meanData.getData().get(0).getValue(), value1.getValue());

    // 2016
    foundData = meanDao.findByPeriod("MEG", 2016, 1);
    assertTrue(foundData.isPresent());

    // Verify 2016 data
    meanData = foundData.get();
    assertEquals(meanData.getData().size(), 2);
    assertEquals(meanData.getData().get(0).getDate(), date2);
    assertEquals(meanData.getData().get(0).getValue(), value2.getValue());

    assertEquals(meanData.getData().get(1).getDate(), date3);
    assertEquals(meanData.getData().get(1).getValue(), value3.getValue());
  }

  @Test
  public void testUpsertSorting() {
    LocalDate date1 = LocalDate.of(2016, 1, 1);
    LocalDate date2 = LocalDate.of(2016, 1, 2);
    LocalDate date3 = LocalDate.of(2016, 1, 3);

    Value value1 = new Value(date1, 1d);
    Value value2 = new Value(date2, 2d);
    Value value3 = new Value(date3, 3d);

    // Save data
    meanDao.save("X", 1, Lists.newArrayList(value3, value1, value2));

    // Retrieve data
    Optional<MeanData> foundData = meanDao.findByPeriod("X", 2016, 1);

    // Verify size
    MeanData result = foundData.get();
    assertEquals(result.getData().size(), 3);

    // Verify order of data
    Value valueResult = result.getData().get(0);
    assertEquals(valueResult.getDate(), date1);

    valueResult = result.getData().get(1);
    assertEquals(valueResult.getDate(), date2);

    valueResult = result.getData().get(2);
    assertEquals(valueResult.getDate(), date3);
  }
}
