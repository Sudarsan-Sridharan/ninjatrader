package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.SystemSetting;
import org.jongo.MongoCollection;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 7/28/16.
 */
public class SystemSettingsDaoTest extends AbstractDaoTest {
  private SystemSettingsDao systemSettingsDao;

  @BeforeClass
  public void setup() {
    systemSettingsDao = injector.getInstance(SystemSettingsDao.class);
  }

  @BeforeMethod
  public void cleanup() {
    MongoCollection collection = systemSettingsDao.getMongoCollection();
    collection.remove();
  }

  @Test
  public void testSaveAndFind() {
    SystemSetting setting = SystemSetting.of("TEST", "1");
    systemSettingsDao.save(setting);

    Optional<SystemSetting> foundSetting = systemSettingsDao.find("TEST");
    assertTrue(foundSetting.isPresent());
    assertEquals(foundSetting.get(), setting);

    foundSetting = systemSettingsDao.find("NON_EXISTING_SETTING");
    assertFalse(foundSetting.isPresent());

    foundSetting = systemSettingsDao.find(null);
    assertFalse(foundSetting.isPresent());
  }

  @Test
  public void testSaveWithOverwrite() {
    SystemSetting setting = SystemSetting.of("TEST", "1");
    systemSettingsDao.save(setting);

    setting = SystemSetting.of("TEST", "2");
    systemSettingsDao.save(setting);

    Optional<SystemSetting> foundSetting = systemSettingsDao.find("TEST");
    assertTrue(foundSetting.isPresent());
    assertEquals(foundSetting.get().getValue(), "2");
  }
}
