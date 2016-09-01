package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.TestUtil;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;


/**
 * Created by Brad on 7/11/16.
 */
public class PriceTest {

  private LocalDate date1 = LocalDate.of(2016, 1, 1);
  private LocalDate date2 = LocalDate.of(2016, 1, 2);

  private Price price1 = new Price(date1, 5.0, 10.0, 4.0, 6.0, 10000);
  private Price price2 = new Price(date1, 5.0, 10.0, 4.0, 6.0, 10000);
  private Price price3 = new Price(date1, 5.1, 10.0, 4.0, 6.0, 10000);
  private Price price4 = new Price(date1, 5.0, 10.1, 4.0, 6.0, 10000);
  private Price price5 = new Price(date1, 5.0, 10.0, 4.1, 6.0, 10000);
  private Price price6 = new Price(date1, 5.0, 10.0, 4.0, 6.1, 10000);
  private Price price7 = new Price(date1, 5.0, 10.0, 4.0, 6.0, 10001);
  private Price price8 = new Price(date2, 5.0, 10.0, 4.0, 6.0, 10000);

  @Test
  public void testHashCode() {
    assertEquals(price1.hashCode(), price2.hashCode());

    TestUtil.assertHashCodeNotEquals(price1, price3, price4, price5, price6, price7, price8);
    TestUtil.assertHashCodeNotEquals(price2, price3, price4, price5, price6, price7, price8);
  }

  @Test
  public void testEquals() {
    assertEquals(price1, price1);
    assertEquals(price1, price2);

    TestUtil.assertNotEqualsList(price1, price3, price4, price5, price6, price7, price8);
    TestUtil.assertNotEqualsList(price2, price3, price4, price5, price6, price7, price8);

    assertFalse(price1.equals(new Object()));
    assertFalse(price1.equals(null));
  }
}
