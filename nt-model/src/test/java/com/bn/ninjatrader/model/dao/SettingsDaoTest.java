//package com.bn.ninjatrader.model.dao;
//
//import com.beust.jcommander.internal.Lists;
//import com.bn.ninjatrader.common.data.Setting;
//import com.bn.ninjatrader.model.guice.NtModelTestModule;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
//import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertNotNull;
//
///**
// * Created by Brad on 7/28/16.
// */
//public class SettingsDaoTest {
//  private static final Logger LOG = LoggerFactory.getLogger(SettingsDaoTest.class);
//  private static final String OWNER = "USER";
//  private static final String DIFF_OWNER = "DIFF_OWNER";
//
//  private static Injector injector;
//
//  private SettingsDao settingsDao;
//
//  @BeforeClass
//  public static void setup() {
//    injector = Guice.createInjector(new NtModelTestModule());
//  }
//
//  @Before
//  public void before() {
//    settingsDao = injector.getInstance(SettingsDao.class);
//    settingsDao.getMongoCollection().remove();
//  }
//
//  @After
//  public void after() {
//    settingsDao.getMongoCollection().remove();
//  }
//
//  @Test
//  public void testSaveAndFind() {
//    final Setting setting1 = Setting.of("KEY_1", "1");
//    final Setting setting2 = Setting.of("KEY_2", "2");
//    settingsDao.save(OWNER, Lists.newArrayList(setting1, setting2));
//
//    final List<Setting> settings = settingsDao.find(OWNER);
//    assertNotNull(settings);
//    assertEquals(settings.size(), 2);
//  }
//
//  @Test
//  public void testFindNonExisting() {
//    final List<Setting> settings = settingsDao.find("NON_EXISTING_SETTING");
//    assertNotNull(settings);
//    assertEquals(settings.size(), 0);
//  }
//
//  @Test
//  public void testSaveWithOverwrite() {
//    Setting setting = Setting.of("TEST", "1");
//    settingsDao.save(OWNER, Lists.newArrayList(setting));
//
//    setting = Setting.of("TEST", "2");
//    settingsDao.save(OWNER, Lists.newArrayList(setting));
//
//    final List<Setting> settings = settingsDao.find(OWNER);
//    assertEquals(settings.size(), 1);
//  }
//
//  @Test
//  public void testWithDiffOwners() {
//    Setting setting = Setting.of("TEST", "1");
//    settingsDao.save(OWNER, Lists.newArrayList(setting));
//
//    setting = Setting.of("TEST", "2");
//    settingsDao.save(DIFF_OWNER, Lists.newArrayList(setting));
//
//    List<Setting> settings = settingsDao.find(OWNER);
//    assertEquals(settings.size(), 1);
//    assertEquals(settings.get(0).getValue(), "1");
//
//    settings = settingsDao.find(DIFF_OWNER);
//    assertEquals(settings.size(), 1);
//    assertEquals(settings.get(0).getValue(), "2");
//  }
//
//  @Test
//  public void testWithDiffOwnersOverwrite() {
//    settingsDao.save(OWNER, Setting.of("TEST", "1"));
//    settingsDao.save(DIFF_OWNER, Setting.of("TEST", "2"));
//    settingsDao.save(DIFF_OWNER, Setting.of("TEST", "OVERWRITTEN_VALUE"));
//
//    List<Setting> settings = settingsDao.find(OWNER);
//    assertEquals(settings.size(), 1);
//    assertEquals(settings.get(0).getValue(), "1");
//
//    settings = settingsDao.find(DIFF_OWNER);
//    assertEquals(settings.size(), 1);
//    assertEquals(settings.get(0).getValue(), "OVERWRITTEN_VALUE");
//  }
//}
