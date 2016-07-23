package com.bn.ninjatrader.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Brad on 6/10/16.
 */
public class NumUtil {

  private NumUtil() {


  }

  public static double toDouble(String text) {
    if (StringUtils.isBlank(text)) {
      throw new IllegalArgumentException("Cannot convert to double. text must not be null or empty.");
    }
    text = text.replaceAll(",", "");
    return Double.parseDouble(text);
  }

  public static long toLong(String text) {
    if (StringUtils.isBlank(text)) {
      throw new IllegalArgumentException("Cannot convert to long. text must not be null or empty.");
    }
    text = text.replaceAll(",", "");
    return Long.parseLong(text);
  }
}
