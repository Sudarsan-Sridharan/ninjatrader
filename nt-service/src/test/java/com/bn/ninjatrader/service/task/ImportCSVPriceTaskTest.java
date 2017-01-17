package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.history.CsvPriceImporter;
import com.google.common.collect.ImmutableMultimap;
import org.junit.Test;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportCSVPriceTaskTest {

  private final PrintWriter printWriter = mock(PrintWriter.class);
  private final CsvPriceImporter csvPriceImporter = mock(CsvPriceImporter.class);
  private final ImportCSVPriceTask importPriceTask = new ImportCSVPriceTask(csvPriceImporter);

  @Test
  public void execute_shouldImportPrices() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder().build();

    importPriceTask.execute(map, printWriter);

    verify(csvPriceImporter, times(1)).importPrices();
  }
}
