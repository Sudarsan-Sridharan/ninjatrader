package com.bn.ninjatrader.common.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 7/29/16.
 */
public class NumUtilTest {

  @Test
  public void testToLong() {
    assertEquals(NumUtil.toLong("0"), 0);
    assertEquals(NumUtil.toLong("-0"), 0);
    assertEquals(NumUtil.toLong(" 123 "), 123);
    assertEquals(NumUtil.toLong("1.0"), 1);
    assertEquals(NumUtil.toLong("-1000"), -1000);
    assertEquals(NumUtil.toLong("-1,000"), -1000);
    assertEquals(NumUtil.toLong("1,000,000,000,000,000,000"), 1000000000000000000l);
  }

  @Test
  public void testToLongOrDefault() {
    long defaultValue = 12345;

    assertEquals(NumUtil.toLongOrDefault("1", defaultValue), 1);

    assertEquals(NumUtil.toLongOrDefault(null, defaultValue), defaultValue);
    assertEquals(NumUtil.toLongOrDefault("", defaultValue), defaultValue);
    assertEquals(NumUtil.toLongOrDefault(" ", defaultValue), defaultValue);
    assertEquals(NumUtil.toLongOrDefault("2x", defaultValue), defaultValue);
    assertEquals(NumUtil.toLongOrDefault("10x0", defaultValue), defaultValue);
    assertEquals(NumUtil.toLongOrDefault("10.0A", defaultValue), defaultValue);
  }

  @Test
  public void testToDouble() {
    assertEquals(NumUtil.toDouble("0"), 0d);
    assertEquals(NumUtil.toDouble("0.0"), 0d);
    assertEquals(NumUtil.toDouble("-0"), -0d);
    assertEquals(NumUtil.toDouble("1.01"), 1.01d);
    assertEquals(NumUtil.toDouble(" 1.01 "), 1.01d);
    assertEquals(NumUtil.toDouble("1.00000000000001"), 1.00000000000001d);
    assertEquals(NumUtil.toDouble("-1000"), -1000d);
    assertEquals(NumUtil.toDouble("-1,000"), -1000d);
    assertEquals(NumUtil.toDouble("1,000,000,000,000,000,000.0001"), 1000000000000000000.0001d);
  }

  @Test
  public void testToDoubleOrDefault() {
    double defaultValue = 12345;

    assertEquals(NumUtil.toDoubleOrDefault("1", defaultValue), 1d);

    assertEquals(NumUtil.toDoubleOrDefault(null, defaultValue), defaultValue);
    assertEquals(NumUtil.toDoubleOrDefault("", defaultValue), defaultValue);
    assertEquals(NumUtil.toDoubleOrDefault(" ", defaultValue), defaultValue);
    assertEquals(NumUtil.toDoubleOrDefault("2x", defaultValue), defaultValue);
    assertEquals(NumUtil.toDoubleOrDefault("10x0", defaultValue), defaultValue);
    assertEquals(NumUtil.toDoubleOrDefault("10.0A", defaultValue), defaultValue);
  }

  @Test
  public void testAddDecimals() {
    assertEquals(NumUtil.plus(5.6, 5.8), 11.4);
    assertEquals(NumUtil.plus(5.6, 5.8001), 11.4001);
    assertEquals(NumUtil.plus(5.6, 5.80009), 11.40009);
    assertEquals(NumUtil.plus(5.6, -5.8d), -0.2);
  }

  @Test
  public void testMinusDecimals() {
    assertEquals(NumUtil.minus(4.8, 0.4), 4.4);
    assertEquals(NumUtil.minus(4.8, 3), 1.8);
    assertEquals(NumUtil.minus(4.8, 0.0002), 4.7998);
    assertEquals(NumUtil.minus(4.8, 0.0094), 4.7906);
  }

  @Test
  public void testDivideDecimals() {
    assertEquals(NumUtil.divide(100, 1), 100d);
    assertEquals(NumUtil.divide(10, 3), 3.333333);
    assertEquals(NumUtil.divide(96.74, 100), 0.9674);
    assertEquals(NumUtil.divide(2.1, 1000), 0.0021);
    assertEquals(NumUtil.divide(2.1, 0.0001), 21000d);
  }

  @Test
  public void testMultiplyDecimals() {
    assertEquals(NumUtil.multiply(1.2, 9), 10.8);
    assertEquals(NumUtil.multiply(95, 1.1), 104.5);
    assertEquals(NumUtil.multiply(68, 1.1), 74.8);
    assertEquals(NumUtil.multiply(100, 0), 0d);
    assertEquals(NumUtil.multiply(100, 0.000001), 0.0001d);
    assertEquals(NumUtil.multiply(3, 3.3333), 9.9999);
  }

  @Test
  public void testMaxOfArray() {
    assertEquals(50, NumUtil.max(new int[] {50}));
    assertEquals(50, NumUtil.max(new int[] {0, 10, 20, 30, 50, 40, -1}));
  }

  @Test
  public void testTrimPrice() {
    assertEquals(NumUtil.trimPrice(0.00019999), 0.0001);
    assertEquals(NumUtil.trimPrice(0.04999), 0.0499);
    assertEquals(NumUtil.trimPrice(Double.NaN), Double.NaN);
    assertEquals(NumUtil.trimPrice(Double.POSITIVE_INFINITY), Double.POSITIVE_INFINITY);
  }

  @Test
  public void testTrim() {
    assertEquals(NumUtil.trim(1.234567, 0), 1.0);
    assertEquals(NumUtil.trim(1.234567, 2), 1.23);
    assertEquals(NumUtil.trim(1.234567, 4), 1.2345);
    assertEquals(NumUtil.trim(-1.234567, 4), -1.2345);
    assertEquals(NumUtil.trim(1.234567, 10), 1.234567);
    assertEquals(NumUtil.trim(Double.NaN, 5), Double.NaN);
    assertEquals(NumUtil.trim(Double.POSITIVE_INFINITY, 5), Double.POSITIVE_INFINITY);
  }

  @Test
  public void testRound() {
    assertEquals(NumUtil.round(1.234567, 0), 1.0);
    assertEquals(NumUtil.round(1.5001, 0), 2.0);
    assertEquals(NumUtil.round(1.234567, 5), 1.23457);
    assertEquals(NumUtil.round(-1.234567, 5), -1.23457);
    assertEquals(NumUtil.round(1.234567, 10), 1.234567);
    assertEquals(NumUtil.round(Double.NaN, 5), Double.NaN);
    assertEquals(NumUtil.round(Double.POSITIVE_INFINITY, 5), Double.POSITIVE_INFINITY);
  }

  @Test
  public void testToPercent() {
    assertEquals(NumUtil.toPercent(0.50), 50d);
    assertEquals(NumUtil.toPercent(0.53999), 53.99d);
    assertEquals(NumUtil.toPercent(100), 10000d);
    assertEquals(NumUtil.toPercent(Double.NaN), Double.NaN);
    assertEquals(NumUtil.toPercent(Double.POSITIVE_INFINITY), Double.POSITIVE_INFINITY);
  }
}
