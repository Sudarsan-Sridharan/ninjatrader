package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.model.DailyQuote;
import com.bn.ninjatrader.common.rest.ImportQuotesRequest;
import com.bn.ninjatrader.dataimport.daily.PseTraderDailyPriceImporter;
import com.bn.ninjatrader.messaging.Message;
import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.service.event.EventTypes;
import com.bn.ninjatrader.service.event.message.ImportedFullPricesMessage;
import com.bn.ninjatrader.service.util.EventIntegrationTest;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportPSETraderDailyQuotesTaskTest extends EventIntegrationTest {
  private static final Logger LOG = LoggerFactory.getLogger(ImportPSETraderDailyQuotesTaskTest.class);
  private static final LocalDate now = LocalDate.of(2016, 2, 1);
  private static final PseTraderDailyPriceImporter importer = mock(PseTraderDailyPriceImporter.class);
  private static final Clock clock = TestUtil.fixedClock(now);
  private static final MessageListener messageListener = mock(MessageListener.class);

  private static ResourceConfig resourceConfig;

  @BeforeClass
  public static void beforeClass() {
    final Multimap<String, MessageListener> subscribers = ArrayListMultimap.create();
    subscribers.put(EventTypes.IMPORTED_FULL_PRICES, messageListener);

    final ImportPSETraderDailyQuotesTask resource = new ImportPSETraderDailyQuotesTask(importer, clock);

    resourceConfig = integrateApplication(subscribers).register(resource);
  }

  @AfterClass
  public static void afterClass() {
    shutDown();
  }

  @Override
  protected Application configure() {
    return resourceConfig;
  }

  @Before
  public void before() {
    reset(importer);
    reset(messageListener);
  }

  @Test
  public void testImportWithDateArgs_shouldImportDataForGivenDates() {
    final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

    final LocalDate date1 = LocalDate.of(2016, 2, 1);
    final LocalDate date2 = LocalDate.of(2016, 2, 2);

    final ImportQuotesRequest request = new ImportQuotesRequest();
    request.setDates(Lists.newArrayList(date1, date2));

    final Response response = target("/tasks/import-pse-trader-quotes")
        .request()
        .post(Entity.json(request));

    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    verify(importer).importData(captor.capture());
    assertThat(captor.getValue()).containsExactly(date1, date2);
  }

  @Test
  public void testImportWithNoDateArg_shouldImportDataForToday() {
    final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

    final Response response = target("/tasks/import-pse-trader-quotes")
        .request()
        .post(Entity.json(new ImportQuotesRequest()));

    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    verify(importer).importData(captor.capture());
    assertThat(captor.getValue()).containsExactly(now);
  }

  @Test
  public void testImport_shouldSendImportEvent() {
    final ArgumentCaptor<ImportedFullPricesMessage> captor =
        ArgumentCaptor.forClass(ImportedFullPricesMessage.class);

    final ImportQuotesRequest request = new ImportQuotesRequest();
    request.setDates(Lists.newArrayList(now));

    final DailyQuote expectedQuote = new DailyQuote("MEG", now, 1, 2, 3, 4, 1000);

    when(importer.importData(anyCollection())).thenReturn(Lists.newArrayList(expectedQuote));

    target("/tasks/import-pse-trader-quotes").request().post(Entity.json(request));

    // Verify that message is sent and handled.
    verify(messageListener).onMessage(captor.capture(), any());

    // Verify payload contains expected quote
    assertThat(captor.getValue().getPayload()).containsExactly(expectedQuote);
  }

  @Test
  public void testImportViaGet_shouldImportTodayQuotes() throws InterruptedException {
    final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
    final ArgumentCaptor<Message<List>> messageCaptor = ArgumentCaptor.forClass(Message.class);

    final Response response = target("/tasks/import-pse-trader-quotes").request().get();

    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    verify(importer).importData(captor.capture());
    assertThat(captor.getValue()).containsExactly(now);

    Thread.sleep(100); // Give topic some time to

    // Verify that message is sent and handled.
    verify(messageListener).onMessage(messageCaptor.capture(), any());
  }
}
