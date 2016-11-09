package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.adaptor.IchimokuDataMapAdaptor;
import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.bn.ninjatrader.simulation.condition.Conditions.*;
import static com.bn.ninjatrader.simulation.data.DataType.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/6/16.
 */
public class ConditionsTest {

  private BarData barParameters;
  private Price price;
  private Ichimoku ichimoku;

  @BeforeClass
  public void setup() {
    price = new Price();
    price.setOpen(1.1);
    price.setHigh(2.2);
    price.setLow(3.3);
    price.setClose(4.4);
    price.setVolume(100000);

    ichimoku = new Ichimoku();
    ichimoku.setChikou(1.1);
    ichimoku.setTenkan(1.2);
    ichimoku.setKijun(1.3);
    ichimoku.setSenkouA(1.4);
    ichimoku.setSenkouB(1.5);

    barParameters = new BarData();
    barParameters.put(new PriceDataMapAdaptor().toDataMap(price));
    barParameters.put(new IchimokuDataMapAdaptor().toDataMap(ichimoku));
  }

  @Test
  public void testSingleEqualsCondition() {
    Condition condition = create().add(eq(PRICE_CLOSE, PRICE_HIGH));
    assertFalseCondition(condition);

    condition = create().add(eq(PRICE_OPEN, CHIKOU));
    assertTrueCondition(condition);

    condition = create().add(eq(PRICE_OPEN, 1.1));
    assertTrueCondition(condition);

    condition = create().add(eq(PRICE_OPEN, 1.11));
    assertFalseCondition(condition);
  }

  @Test
  public void testMultipleEqualsCondition() {
    AndCondition condition = create()
        .add(eq(PRICE_OPEN, CHIKOU))
        .add(eq(PRICE_OPEN, 1.1))
        .add(eq(PRICE_HIGH, 2.2))
        .add(eq(PRICE_LOW, 3.3))
        .add(eq(PRICE_CLOSE, 4.4));
    assertTrueCondition(condition);

    condition.add(eq(PRICE_OPEN, 0));
    assertFalseCondition(condition);
  }

  @Test
  public void testGreaterThanCondition() {
    Condition condition = create().add(gt(PRICE_OPEN, 1.1));
    assertFalseCondition(condition);

    condition = create().add(gt(PRICE_OPEN, 1.099));
    assertTrueCondition(condition);

    condition = create()
        .add(gt(PRICE_CLOSE, PRICE_LOW))
        .add(gt(PRICE_HIGH, PRICE_OPEN))
        .add(gt(SENKOU_B, 1.4));
    assertTrueCondition(condition);

    condition = create()
        .add(gt(PRICE_CLOSE, PRICE_LOW))
        .add(gt(PRICE_HIGH, PRICE_OPEN))
        .add(gt(SENKOU_B, 1.5));
    assertFalseCondition(condition);
  }

  @Test
  public void testGreaterThanOrEqualsCondition() {
    Condition condition = create().add(gte(PRICE_OPEN, 1.1));
    assertTrueCondition(condition);

    condition = create().add(gte(PRICE_OPEN, 1.11));
    assertFalseCondition(condition);

    condition = create()
        .add(gte(CHIKOU, 1.1))
        .add(gte(TENKAN, CHIKOU))
        .add(gte(PRICE_OPEN, CHIKOU));
    assertTrueCondition(condition);

    condition = create()
        .add(gte(CHIKOU, 1.11))
        .add(gte(TENKAN, CHIKOU))
        .add(gte(PRICE_OPEN, CHIKOU));
    assertFalseCondition(condition);
  }

  private void assertTrueCondition(Condition condition) {
    assertTrue(condition.isMatch(barParameters));
  }

  private void assertFalseCondition(Condition condition) {
    assertFalse(condition.isMatch(barParameters));
  }
}
