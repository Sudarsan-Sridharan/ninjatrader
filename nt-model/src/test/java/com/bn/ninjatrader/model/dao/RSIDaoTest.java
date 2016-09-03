package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.RSIValue;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.request.SaveRequest.save;
import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 5/4/16.
 */
public class RSIDaoTest extends AbstractValueDaoTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);

  private final RSIValue rsiValue1 = new RSIValue(now, 35.5, 0.22, 0.10);

  @Override
  public RSIDao provideTestedDao() {
    return getInjector().getInstance(RSIDao.class);
  }

  @Test
  public void testSaveRsiValue() {
    RSIDao dao = provideTestedDao();

    dao.save(SaveRequest.save("MEG").period(1).values(rsiValue1));

    List<RSIValue> result = dao.find(FindRequest.forSymbol("MEG").period(1));

    assertEquals(result.size(), 1);
    RSIValue value = result.get(0);

    assertEquals(value.getDate(), rsiValue1.getDate());
    assertEquals(value.getValue(), rsiValue1.getValue());
    assertEquals(value.getAvgGain(), rsiValue1.getAvgGain());
    assertEquals(value.getAvgLoss(), rsiValue1.getAvgLoss());
  }

  @Test
  public void testSaveOverwriteRsi() {
    RSIDao dao = provideTestedDao();

    Value value = Value.of(now, 100);
    dao.save(save("MEG").period(1).values(value));

    RSIValue overwriteValue = new RSIValue(now, 10d, 1.01, 2.02);
    dao.save(save("MEG").period(1).values(overwriteValue));

    List<RSIValue> result = dao.find(FindRequest.forSymbol("MEG").period(1).from(now).to(now));

    assertEquals(result.size(), 1);
  }
}
