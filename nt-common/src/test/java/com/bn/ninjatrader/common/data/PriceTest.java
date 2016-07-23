package com.bn.ninjatrader.common.data;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;


/**
 * Created by Brad on 7/11/16.
 */
public class PriceTest {

  private Price price1 = new Price(5.0, 10.0, 4.0, 6.0, 10000);
  private Price price2 = new Price(5.0, 10.0, 4.0, 6.0, 10000);
  private Price price3 = new Price(5.1, 10.0, 4.0, 6.0, 10000);
  private Price price4 = new Price(5.0, 10.1, 4.0, 6.0, 10000);
  private Price price5 = new Price(5.0, 10.0, 4.1, 6.0, 10000);
  private Price price6 = new Price(5.0, 10.0, 4.0, 6.1, 10000);
  private Price price7 = new Price(5.0, 10.0, 4.0, 6.0, 10001);

  @Test
  public void testHashCode() {
    assertEquals(price1.hashCode(), price2.hashCode());

    assertHashCodeNotEquals(price1, price3, price4, price5, price6, price7);
    assertHashCodeNotEquals(price2, price3, price4, price5, price6, price7);
  }

  @Test
  public void testEquals() {
    assertEquals(price1, price1);
    assertEquals(price1, price2);

    assertPriceNotEquals(price1, price3, price4, price5, price6, price7);
    assertPriceNotEquals(price2, price3, price4, price5, price6, price7);

    assertFalse(price1.equals(new Object()));
    assertFalse(price1.equals(null));
  }

  private void assertHashCodeNotEquals(Price price, Price ... prices) {
    for (Price p : prices) {
      assertNotEquals(price.hashCode(), p.hashCode(), "Expecting hashcode to be not equal for " + p);
    }
  }

  private void assertPriceNotEquals(Price price, Price ... prices) {
    for (Price p : prices) {
      assertNotEquals(price, p, "Expecting price " + price + "not equal to: " + p);
    }
  }
}
