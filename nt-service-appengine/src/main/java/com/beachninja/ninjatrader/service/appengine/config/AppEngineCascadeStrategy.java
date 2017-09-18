package com.beachninja.ninjatrader.service.appengine.config;

import com.google.appengine.api.utils.SystemProperty;
import com.google.common.collect.Lists;
import com.netflix.archaius.api.CascadeStrategy;
import com.netflix.archaius.api.StrInterpolator;
import com.netflix.archaius.cascade.ConcatCascadeStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AppEngineCascadeStrategy implements CascadeStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(AppEngineCascadeStrategy.class);
  private static final String PROD = "prod";
  private static final String DEV = "dev";

  private final CascadeStrategy cascadeStrategy;

  @Inject
  public AppEngineCascadeStrategy() {
    final List<String> env = Lists.newArrayList();

    switch (SystemProperty.environment.value()) {
      case Production: env.add(PROD); break;
      case Development: env.add(DEV); break;
    }

    cascadeStrategy = new ConcatCascadeStrategy(env);
  }

  @Override
  public List<String> generate(String s, StrInterpolator strInterpolator, StrInterpolator.Lookup lookup) {
    final List<String> result = cascadeStrategy.generate(s, strInterpolator, lookup);
    LOG.info("Loading property files: {}", result);
    return result;
  }
}

