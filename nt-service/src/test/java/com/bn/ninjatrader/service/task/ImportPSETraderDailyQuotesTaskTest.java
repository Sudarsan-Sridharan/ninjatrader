package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.daily.PseTraderDailyPriceImporter;
import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.service.model.ImportQuotesRequest;
import com.google.common.collect.Lists;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportPSETraderDailyQuotesTaskTest extends JerseyTest {

  private static final LocalDate now = LocalDate.of(2016, 2, 1);
  private static final PseTraderDailyPriceImporter importer = mock(PseTraderDailyPriceImporter.class);
  private static final Clock clock = TestUtil.fixedClock(now);

  @Override
  protected Application configure() {
    final ImportPSETraderDailyQuotesTask resource = new ImportPSETraderDailyQuotesTask(importer, clock);
    return new ResourceConfig().register(resource);
  }

  @Before
  public void before() {
    reset(importer);
  }

  @Test
  public void testImportWithDateArgs_shouldImportDataForGivenDates() {
    final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

    final LocalDate date1 = LocalDate.of(2016, 2, 1);
    final LocalDate date2 = LocalDate.of(2016, 2, 2);

    final ImportQuotesRequest request = new ImportQuotesRequest();
    request.setDates(Lists.newArrayList(date1, date2));

    final Response response = target("/task/import-pse-trader-quotes").request().post(Entity.json(request));

    assertThat(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode());

    verify(importer).importData(captor.capture());
    assertThat(captor.getValue()).containsExactly(date1, date2);
  }

  @Test
  public void testImportWithNoDateArg_shouldImportDataForToday() {
    final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

    final Response response = target("/task/import-pse-trader-quotes").request().post(Entity.json(new ImportQuotesRequest()));

    assertThat(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode());

    verify(importer).importData(captor.capture());
    assertThat(captor.getValue()).containsExactly(now);
  }
}
