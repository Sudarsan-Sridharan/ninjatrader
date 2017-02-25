package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.daily.PseDailyPriceImporter;
import com.bn.ninjatrader.model.util.TestUtil;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportPSEDailyQuotesTaskTest extends JerseyTest {

  private static final LocalDate now = LocalDate.of(2016, 2, 1);
  private static final PseDailyPriceImporter importer = mock(PseDailyPriceImporter.class);
  private static final Clock clock = TestUtil.fixedClock(now);

  @Override
  protected Application configure() {
    final ImportPSEDailyQuotesTask resource = new ImportPSEDailyQuotesTask(importer, clock);
    return new ResourceConfig().register(resource);
  }

  @Test
  public void testImportPseQuotes_shouldImportQuotes() throws Exception {
    final Response response = target("/task/import-pse-quotes").request().post(Entity.form(new Form()));
    assertThat(response.getStatus()).isEqualTo(204);

    verify(importer).importData(now);
  }
}
