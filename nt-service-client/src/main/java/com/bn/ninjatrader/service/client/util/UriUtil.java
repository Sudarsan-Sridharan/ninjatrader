package com.bn.ninjatrader.service.client.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author bradwee2000@gmail.com
 */
public class UriUtil {

  private UriUtil() {}

  public static String normalize(final String absolutePath) {
    try {
      return new URI(absolutePath).normalize().toString();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
