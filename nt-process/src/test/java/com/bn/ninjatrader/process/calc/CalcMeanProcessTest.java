package com.bn.ninjatrader.process.calc;

import com.beust.jcommander.internal.Maps;
import com.bn.ninjatrader.calculator.MeanCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.dao.MeanDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.data.PriceData;
import com.google.common.collect.Lists;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 6/11/16.
 */
public class CalcMeanProcessTest {

  private static final Logger log = LoggerFactory.getLogger(CalcMeanProcessTest.class);

  @Injectable
  private MeanCalculator meanCalculator;

  @Injectable
  private MeanDao meanDao;

  @Injectable
  private PriceDao priceDao;

  @Tested
  private CalcMeanProcess process;

  private Random random = new Random();

  @BeforeClass
  private void setupData() {
    PriceData data1 = new PriceData("MEG", 2016);
    data1.getData().addAll(createPrices(100));
  }

  @Test
  public void testProcessWithNoData() {
    process.process("MEG", LocalDate.of(2015, 1, 1), LocalDate.of(2016, 1, 1));
  }

  @Test
  public void testProcessWithData() {
    new Expectations() {{
      LocalDate date1 = LocalDate.of(2014, 1, 1);
      LocalDate date2 = LocalDate.of(2016, 1, 1);
      Map<Integer, List<Value>> valueMap1 = Maps.newHashMap();
      valueMap1.put(9, Lists.newArrayList(new Value(date1, 100d)));
      valueMap1.put(26, Lists.newArrayList(new Value(date1, 200d)));
      valueMap1.put(52, Lists.newArrayList(new Value(date1, 300d), new Value(date2, 400d)));
      meanCalculator.calc(withInstanceOf(List.class), CalcMeanProcess.PERIODS); result = valueMap1;
    }};

    process.process("MEG", LocalDate.of(2015, 1, 1), LocalDate.of(2016, 1, 1));

    // Verify save is called for each symbol, year, and period.
    new Verifications() {{
      List<Value> values;

      meanDao.save("MEG", 9, values = withCapture()); times = 1;
      assertEquals(values.get(0).getValue(), 100d);

      meanDao.save("MEG", 26, values = withCapture()); times = 1;
      assertEquals(values.get(0).getValue(), 200d);

      meanDao.save("MEG", 52, values = withCapture()); times = 1;
      assertEquals(values.get(0).getValue(), 300d);
      assertEquals(values.get(1).getValue(), 400d);
    }};
  }

  private List<Price> createPrices(int count) {
    List<Price> prices = Lists.newArrayList();
    LocalDate date = LocalDate.of(2016, 1, 1);

    for (int i = 0; i < count; i++) {
      Price price = new Price();
      price.setDate(date);
      price.setOpen(random.nextDouble());
      price.setHigh(random.nextDouble());
      price.setLow(random.nextDouble());
      price.setClose(random.nextDouble());
      price.setVolume(random.nextLong());
      prices.add(price);

      date = date.plusDays(1);
    }
    return prices;
  }
}
