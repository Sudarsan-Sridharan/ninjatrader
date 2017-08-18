package com.bn.ninjatrader.service.client.service;

import com.bn.ninjatrader.common.model.DailyQuote;
import com.bn.ninjatrader.common.rest.ImportQuotesRequest;
import com.bn.ninjatrader.service.client.annotation.ServiceUrlProperty;
import com.bn.ninjatrader.service.client.util.UriUtil;
import com.netflix.archaius.api.Property;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ImportQuotesClient {
  private static final Logger LOG = LoggerFactory.getLogger(ImportQuotesClient.class);
  private static final String MISSING_SERVICE_HOST = "Service Host property is required.";
  private final Client restClient;
  private final Property<String> serviceUrlProperty;

  @Inject
  public ImportQuotesClient(final Client restClient,
                            @ServiceUrlProperty final Property<String> serviceUrlProperty) {
    this.restClient = restClient;
    this.serviceUrlProperty = serviceUrlProperty;
  }

  public List<DailyQuote> importQuotes(final ImportQuotesRequest req) {
    final String serviceHost = serviceUrlProperty.get();
    checkState(!StringUtils.isEmpty(serviceHost), MISSING_SERVICE_HOST);

    final String target = UriUtil.normalize(
        String.format("%s/tasks/import-pse-trader-quotes", serviceUrlProperty.get()));

    final Response response = restClient.target(target)
        .request()
        .post(Entity.entity(req, APPLICATION_JSON_TYPE));

    final List<DailyQuote> importedQuotes = response.readEntity(new GenericType<List<DailyQuote>>() {});

    return importedQuotes;
  }
}
