package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.service.model.RenameStockSymbolRequest;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class RenameStockSymbolTaskTest extends JerseyTest {

  private PriceDao priceDao;
  private PriceDao.RenameSymbolOperation renameOperation;

  @Override
  protected Application configure() {
    priceDao = mock(PriceDao.class);
    final RenameStockSymbolTask task = new RenameStockSymbolTask(priceDao);
    return new ResourceConfig().register(task);
  }

  @Before
  public void before() {
    reset(priceDao);
    renameOperation = mock(PriceDao.RenameSymbolOperation.class);

    when(priceDao.renameSymbol(anyString())).thenReturn(renameOperation);
    when(renameOperation.to(anyString())).thenReturn(renameOperation);
  }

  @Test
  public void testRun_shouldRenameSymbol() {
    final RenameStockSymbolRequest req = new RenameStockSymbolRequest();
    req.setFrom("MEG");
    req.setTo("MEG2");

    final Response response = target("/tasks/rename-stock-symbol/run")
        .request()
        .post(Entity.json(req));

    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    verify(priceDao).renameSymbol("MEG");
    verify(renameOperation).to("MEG2");
    verify(renameOperation).now();
  }
}
