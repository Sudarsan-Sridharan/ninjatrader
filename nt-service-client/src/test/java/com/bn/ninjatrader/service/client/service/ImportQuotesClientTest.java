package com.bn.ninjatrader.service.client.service;

import com.bn.ninjatrader.auth.guice.NtAuthModule;
import com.bn.ninjatrader.auth.token.TokenGenerator;
import com.bn.ninjatrader.common.model.DailyQuote;
import com.bn.ninjatrader.common.rest.ImportQuotesRequest;
import com.bn.ninjatrader.service.client.filter.SecureClientRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.Property;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportQuotesClientTest extends JerseyTest {
  private static final Logger LOG = LoggerFactory.getLogger(ImportQuotesClientTest.class);

  private static TokenGenerator tokenGenerator;
  private static Property<String> apiSecretKey;

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final DailyQuote quote = new DailyQuote("MEG", LocalDate.of(2016, 2, 1), 1, 2, 3, 4, 1000);
  private final ObjectMapper om = new ObjectMapper();

  private Property<String> serviceHostUrl;
  private ImportQuotesClient importQuotesClient;

  @Override
  protected Application configure() {
    return new ResourceConfig().register(new DummyResource());
  }

  @BeforeClass
  public static void beforeClass() {
    final Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(Config.class).toInstance(mock(Config.class, RETURNS_DEEP_STUBS));
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
      }
    }, new NtAuthModule());

    tokenGenerator = injector.getInstance(TokenGenerator.class);

    apiSecretKey = mock(Property.class);
    when(apiSecretKey.get()).thenReturn("test_secret_key");
  }

  @Before
  public void before() {
    client().register(new SecureClientRequestFilter(tokenGenerator, om, apiSecretKey));

    serviceHostUrl = mock(Property.class);

    when(serviceHostUrl.get()).thenReturn(getBaseUri().toString());

    importQuotesClient = new ImportQuotesClient(client(), serviceHostUrl);
  }

  @Test
  public void testImportQuotes_shouldReturnImportedQuotes() {
    final List<DailyQuote> quotes = importQuotesClient.importQuotes(ImportQuotesRequest.forDate(now));
    assertThat(quotes).containsExactly(quote);
  }

  @Test(expected = IllegalStateException.class)
  public void testImportQuotesWithNoServiceUrl_shouldReturnError() {
    when(serviceHostUrl.get()).thenReturn("");
    importQuotesClient.importQuotes(ImportQuotesRequest.forDate(now));
  }

  /**
   * Dummy Resource
   */
  @Produces(APPLICATION_JSON)
  @Consumes(APPLICATION_JSON)
  @Path("/tasks/import-pse-trader-quotes")
  public class DummyResource {

    @POST
    public Response execute(final ImportQuotesRequest req) {
      return Response.ok(Lists.newArrayList(quote)).build();
    }
  }
}
