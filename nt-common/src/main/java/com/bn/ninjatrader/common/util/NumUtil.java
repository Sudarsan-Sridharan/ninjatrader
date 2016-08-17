package com.bn.ninjatrader.common.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Brad on 6/10/16.
 */
public class NumUtil {

  private static final int DECIMAL_PRECISION = 1000000;

  private NumUtil() {
  }

  public static double toDoubleOrDefault(String text, double defaultValue) {
    try {
      return toDouble(text);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static double toDouble(String text) {
    if (StringUtils.isBlank(text)) {
      throw new IllegalArgumentException("Cannot convert to double. text must not be null or empty.");
    }
    text = text.replaceAll(",", "");
    return Double.parseDouble(text);
  }

  public static long toLongOrDefault(String text, long defaultValue) {
    try {
      return toLong(text);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static long toLong(String text) {
    if (StringUtils.isBlank(text)) {
      throw new IllegalArgumentException("Cannot convert to long. text must not be null or empty.");
    }
    text = text.replaceAll(",", "");
    return (long) Double.parseDouble(text);
  }

  public static double plus(double lhs, double rhs) {
    long result = toBaseUnit(lhs) + toBaseUnit(rhs);
    return toActualUnit(result);
  }

  public static double minus(double lhs, double rhs) {
    long result = toBaseUnit(lhs) - toBaseUnit(rhs);
    return toActualUnit(result);
  }

  public static double divide(double lhs, double rhs) {
    long result = toBaseUnit(lhs / rhs);
    return toActualUnit(result);
  }

  public static double multiply(double lhs, double rhs) {
    long result = toBaseUnit(lhs * rhs);
    return toActualUnit(result);
  }

  private static long toBaseUnit(double value) {
    return Math.round(value * DECIMAL_PRECISION);
  }

  private static double toActualUnit(long baseUnitValue) {
    return (double) baseUnitValue / DECIMAL_PRECISION;
  }

  public static int max(int[] values) {
    Preconditions.checkNotNull(values);
    Preconditions.checkArgument(values.length > 0, "Array must have size > 0.");

    int max = Integer.MIN_VALUE;
    for (int value : values) {
      if (value > max) {
        max = value;
      }
    }
    return max;
  }
}
