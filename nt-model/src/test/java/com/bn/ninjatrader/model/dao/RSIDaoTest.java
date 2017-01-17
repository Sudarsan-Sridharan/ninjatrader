package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.RSIValue;
import com.bn.ninjatrader.model.guice.NtModelTestModule;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 5/4/16.
 */
public class RSIDaoTest {

  private static Injector injector;

  private final LocalDate date1 = LocalDate.of(2016, 1, 1);
  private final RSIValue rsiValue1 = new RSIValue(date1, 35.5, 0.22, 0.10);

  private RSIDao dao;

  @BeforeClass
  public void setupInjector() {
    injector = Guice.createInjector(new NtModelTestModule());
  }

  @BeforeMethod
  public void setup() {
    dao = injector.getInstance(RSIDao.class);
  }

  @Test
  public void testSaveAndFindRsiValue_shouldSaveAndReturnSavedValue() {
    dao.save(SaveRequest.save("MEG").period(1).values(rsiValue1));
    assertThat(dao.find(FindRequest.findSymbol("MEG").period(1))).containsExactly(rsiValue1);
  }
}
