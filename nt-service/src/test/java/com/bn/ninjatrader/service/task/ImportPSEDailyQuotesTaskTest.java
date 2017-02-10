package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.daily.PseDailyPriceImporter;
import com.google.common.collect.ImmutableMultimap;
import org.junit.Test;

import java.io.PrintWriter;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportPSEDailyQuotesTaskTest {

  private final PrintWriter printWriter = mock(PrintWriter.class);
  private final PseDailyPriceImporter importer = mock(PseDailyPriceImporter.class);
  private final CalcTask calcTask = mock(CalcTask.class);
  private final ImportPSEDailyQuotesTask task = new ImportPSEDailyQuotesTask(importer, calcTask);

  @Test
  public void execute_shouldImportPrices() throws Exception {
    final ImmutableMultimap<String, String> args = ImmutableMultimap.<String, String>builder().build();

    task.execute(args, printWriter);

    verify(importer).importData(any(LocalDate.class));
  }
}
