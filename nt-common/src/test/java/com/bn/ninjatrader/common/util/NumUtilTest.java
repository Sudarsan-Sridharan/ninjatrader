package com.bn.ninjatrader.common.util;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 7/29/16.
 */
public class NumUtilTest {

  @Test
  public void testToLong() {
    assertThat(NumUtil.toLong("0")).isEqualTo(0);
    assertThat(NumUtil.toLong("-0")).isEqualTo(0);
    assertThat(NumUtil.toLong(" 123 ")).isEqualTo(123);
    assertThat(NumUtil.toLong("1.0")).isEqualTo(1);
    assertThat(NumUtil.toLong("-1000")).isEqualTo(-1000);
    assertThat(NumUtil.toLong("-1,000")).isEqualTo(-1000);
    assertThat(NumUtil.toLong("1,000,000,000,000,000,000")).isEqualTo(1000000000000000000l);
  }

  @Test
  public void testToLongOrDefault() {
    long defaultValue = 12345;

    assertThat(NumUtil.toLongOrDefault("1", defaultValue)).isEqualTo(1);

    assertThat(NumUtil.toLongOrDefault(null, defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toLongOrDefault("", defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toLongOrDefault(" ", defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toLongOrDefault("2x", defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toLongOrDefault("10x0", defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toLongOrDefault("10.0A", defaultValue)).isEqualTo(defaultValue);
  }

  @Test
  public void testToDouble() {
    assertThat(NumUtil.toDouble("0")).isEqualTo(0d);
    assertThat(NumUtil.toDouble("0.0")).isEqualTo(0d);
    assertThat(NumUtil.toDouble("-0")).isEqualTo(-0d);
    assertThat(NumUtil.toDouble("1.01")).isEqualTo(1.01d);
    assertThat(NumUtil.toDouble(" 1.01 ")).isEqualTo(1.01d);
    assertThat(NumUtil.toDouble("1.00000000000001")).isEqualTo(1.00000000000001d);
    assertThat(NumUtil.toDouble("-1000")).isEqualTo(-1000d);
    assertThat(NumUtil.toDouble("-1,000")).isEqualTo(-1000d);
    assertThat(NumUtil.toDouble("1,000,000,000,000,000,000.0001")).isEqualTo(1000000000000000000.0001d);
  }

  @Test
  public void testToDoubleOrDefault() {
    double defaultValue = 12345;

    assertThat(NumUtil.toDoubleOrDefault("1", defaultValue)).isEqualTo(1d);

    assertThat(NumUtil.toDoubleOrDefault(null, defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toDoubleOrDefault("", defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toDoubleOrDefault(" ", defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toDoubleOrDefault("2x", defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toDoubleOrDefault("10x0", defaultValue)).isEqualTo(defaultValue);
    assertThat(NumUtil.toDoubleOrDefault("10.0A", defaultValue)).isEqualTo(defaultValue);
  }

  @Test
  public void testAddDecimals() {
    assertThat(NumUtil.plus(5.6, 5.8)).isEqualTo(11.4);
    assertThat(NumUtil.plus(5.6, 5.8001)).isEqualTo(11.4001);
    assertThat(NumUtil.plus(5.6, 5.80009)).isEqualTo(11.40009);
    assertThat(NumUtil.plus(5.6, -5.8d)).isEqualTo(-0.2);
  }

  @Test
  public void testMinusDecimals() {
    assertThat(NumUtil.minus(4.8, 0.4)).isEqualTo(4.4);
    assertThat(NumUtil.minus(4.8, 3)).isEqualTo(1.8);
    assertThat(NumUtil.minus(4.8, 0.0002)).isEqualTo(4.7998);
    assertThat(NumUtil.minus(4.8, 0.0094)).isEqualTo(4.7906);
  }

  @Test
  public void testDivideDecimals() {
    assertThat(NumUtil.divide(100, 1)).isEqualTo(100d);
    assertThat(NumUtil.divide(10, 3)).isEqualTo(3.333333);
    assertThat(NumUtil.divide(96.74, 100)).isEqualTo(0.9674);
    assertThat(NumUtil.divide(2.1, 1000)).isEqualTo(0.0021);
    assertThat(NumUtil.divide(2.1, 0.0001)).isEqualTo(21000d);
  }

  @Test
  public void testMultiplyDecimals() {
    assertThat(NumUtil.multiply(1.2, 9)).isEqualTo(10.8);
    assertThat(NumUtil.multiply(95, 1.1)).isEqualTo(104.5);
    assertThat(NumUtil.multiply(68, 1.1)).isEqualTo(74.8);
    assertThat(NumUtil.multiply(100, 0)).isEqualTo(0d);
    assertThat(NumUtil.multiply(100, 0.000001)).isEqualTo(0.0001d);
    assertThat(NumUtil.multiply(3, 3.3333)).isEqualTo(9.9999);
  }

  @Test
  public void testMaxOfArray() {
    assertThat(NumUtil.max(Lists.newArrayList(50))).isEqualTo(50);
    assertThat(NumUtil.max(Lists.newArrayList(0, 10, 20, 30, 50, 40, -1))).isEqualTo(50);
  }

  @Test
  public void testTrimPrice() {
    assertThat(NumUtil.trimPrice(0.00019999)).isEqualTo(0.0001);
    assertThat(NumUtil.trimPrice(0.04999)).isEqualTo(0.0499);
    assertThat(NumUtil.trimPrice(Double.NaN)).isEqualTo(Double.NaN);
    assertThat(NumUtil.trimPrice(Double.POSITIVE_INFINITY)).isEqualTo(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testTrim() {
    assertThat(NumUtil.trim(1.234567, 0)).isEqualTo(1.0);
    assertThat(NumUtil.trim(1.234567, 2)).isEqualTo(1.23);
    assertThat(NumUtil.trim(1.234567, 4)).isEqualTo(1.2345);
    assertThat(NumUtil.trim(-1.234567, 4)).isEqualTo(-1.2345);
    assertThat(NumUtil.trim(1.234567, 10)).isEqualTo(1.234567);
    assertThat(NumUtil.trim(Double.NaN, 5)).isEqualTo(Double.NaN);
    assertThat(NumUtil.trim(Double.POSITIVE_INFINITY, 5)).isEqualTo(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testRound() {
    assertThat(NumUtil.round(1.234567, 0)).isEqualTo(1.0);
    assertThat(NumUtil.round(1.5001, 0)).isEqualTo(2.0);
    assertThat(NumUtil.round(1.234567, 5)).isEqualTo(1.23457);
    assertThat(NumUtil.round(-1.234567, 5)).isEqualTo(-1.23457);
    assertThat(NumUtil.round(1.234567, 10)).isEqualTo(1.234567);
    assertThat(NumUtil.round(Double.NaN, 5)).isEqualTo(Double.NaN);
    assertThat(NumUtil.round(Double.POSITIVE_INFINITY, 5)).isEqualTo(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testToPercent() {
    assertThat(NumUtil.toPercent(0.50)).isEqualTo(50d);
    assertThat(NumUtil.toPercent(0.53999)).isEqualTo(53.99d);
    assertThat(NumUtil.toPercent(100)).isEqualTo(10000d);
    assertThat(NumUtil.toPercent(Double.NaN)).isEqualTo(Double.NaN);
    assertThat(NumUtil.toPercent(Double.POSITIVE_INFINITY)).isEqualTo(Double.POSITIVE_INFINITY);
  }
}
