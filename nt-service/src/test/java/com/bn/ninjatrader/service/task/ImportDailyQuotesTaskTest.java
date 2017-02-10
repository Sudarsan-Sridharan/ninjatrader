package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.dataimport.daily.PseTraderDailyPriceImporter;
import com.google.common.collect.ImmutableMultimap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportDailyQuotesTaskTest {

  private final LocalDate now = LocalDate.of(2016, 2, 5);

  private PseTraderDailyPriceImporter importer;
  private Clock clock;
  private ImportPSETraderDailyQuotesTask task;
  private PrintWriter printWriter;

  @Before
  public void before() {
    importer = mock(PseTraderDailyPriceImporter.class);
    clock = TestUtil.fixedClock(now);
    printWriter = mock(PrintWriter.class);
    task = new ImportPSETraderDailyQuotesTask(importer, clock);

    when(printWriter.append(anyString())).thenReturn(printWriter);
  }

  @Test
  public void testImportWithDateArgs_shouldImportDataForGivenDates() {
    final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

    task.execute(ImmutableMultimap.<String, String>builder()
        .put("date", "20160201")
        .put("date", "20160202")
        .build(), printWriter);

    verify(importer).importData(captor.capture());
    assertThat(captor.getValue()).containsExactly(
        LocalDate.parse("20160201", DateTimeFormatter.BASIC_ISO_DATE),
        LocalDate.parse("20160202", DateTimeFormatter.BASIC_ISO_DATE)
    );
  }

  @Test
  public void testImportWithNoDateArg_shouldImportDataForToday() {
    final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

    task.execute(ImmutableMultimap.<String, String>builder().build(), printWriter);

    verify(importer).importData(captor.capture());
    assertThat(captor.getValue()).containsExactly(now);
  }
}
