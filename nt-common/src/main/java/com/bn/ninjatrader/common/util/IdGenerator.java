package com.bn.ninjatrader.common.util;

import com.google.inject.Singleton;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class IdGenerator {

  private static final int ID_LENGTH = 8;

  public String createId() {
    return RandomStringUtils.randomAlphanumeric(ID_LENGTH);
  }
}
