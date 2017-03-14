package com.bn.ninjatrader.server.util;

import com.bn.ninjatrader.server.config.ServerConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class HtmlWriterFactory {
  private static final String KEY_SERVICE_HOST = "serviceHost";

  private final ServerConfig config;

  @Inject
  public HtmlWriterFactory(final ServerConfig config) {
    this.config = config;
  }

  public HtmlWriter createWithTemplate(final String templatePath) {
    return HtmlWriter.withTemplatePath(templatePath)
        .put(KEY_SERVICE_HOST, config.getServiceHost());
  }
}
