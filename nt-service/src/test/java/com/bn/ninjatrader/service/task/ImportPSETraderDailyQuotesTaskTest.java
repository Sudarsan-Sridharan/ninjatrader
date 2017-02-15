package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.dataimport.daily.PseTraderDailyPriceImporter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    target("/task/import-pse-trader-quotes").request().post(Entity.form(new Form()
        .param("date", "20160201")
        .param("date", "20160202")
    ));

    verify(importer).importData(captor.capture());
    assertThat(captor.getValue()).containsExactly(
        LocalDate.parse("20160201", DateTimeFormatter.BASIC_ISO_DATE),
        LocalDate.parse("20160202", DateTimeFormatter.BASIC_ISO_DATE)
    );
  }

  @Test
  public void testImportWithNoDateArg_shouldImportDataForToday() {
    final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

    target("/task/import-pse-trader-quotes").request().post(Entity.form(new Form()));

    verify(importer).importData(captor.capture());
    assertThat(captor.getValue()).containsExactly(now);
  }
}
