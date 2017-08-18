package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.history.CsvPriceImporter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportCSVPriceTaskTest extends JerseyTest {

  private static final CsvPriceImporter csvPriceImporter = mock(CsvPriceImporter.class);

  @Override
  protected Application configure() {
    final ImportCSVPriceTask task = new ImportCSVPriceTask(csvPriceImporter);
    return new ResourceConfig().register(task);
  }

  @Before
  public void before() {
    reset(csvPriceImporter);
  }

  @Test
  public void testImportCsvPrices_shouldImportPrices() throws Exception {
    final Response response = target("/tasks/importcsvprice").request().post(Entity.form(new Form()));
    assertThat(response.getStatus()).isEqualTo(204);
    verify(csvPriceImporter).importPrices();
  }
}
