package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.data.history.PriceImporter;
import com.google.common.collect.ImmutableMultimap;
import org.junit.Test;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportPSEDailyQuotesTaskTest {

  private final PrintWriter printWriter = mock(PrintWriter.class);
  private final PriceImporter  priceImporter = mock(PriceImporter.class);
  private final ImportCSVPriceTask importPriceTask = new ImportCSVPriceTask(priceImporter);

  @Test
  public void execute_shouldImportPrices() throws Exception {
    final ImmutableMultimap<String, String> map = ImmutableMultimap.<String, String>builder().build();

    importPriceTask.execute(map, printWriter);

    verify(priceImporter, times(1)).importPrices();
  }
}
