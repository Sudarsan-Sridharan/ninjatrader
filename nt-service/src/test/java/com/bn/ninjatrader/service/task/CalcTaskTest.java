package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.process.request.CalcServiceRequest;
import com.bn.ninjatrader.process.service.CalcService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class CalcTaskTest extends JerseyTest {

  private static final LocalDate now = LocalDate.of(2016, 2, 10);
  private static final Clock clock = TestUtil.fixedClock(now);
  private static final CalcService calcService = mock(CalcService.class);

  @Override
  protected Application configure() {
    final CalcTask calcTask = new CalcTask(clock, calcService);
    return new ResourceConfig().register(calcTask);
  }

  @Before
  public void before() {
    reset(calcService);
  }

  @Test
  public void testRunWithNoInputDate_shouldRunProcessForToday() {
    final ArgumentCaptor<CalcServiceRequest> captor = ArgumentCaptor.forClass(CalcServiceRequest.class);

    target("/task/calc").request().post(Entity.form(new Form()
        .param("process", "process1")
        .param("symbol", "MEG")
    ));

    verify(calcService).calc(captor.capture());
    assertThat(captor.getValue().getSymbols()).containsExactly("MEG");
    assertThat(captor.getValue().getFrom()).hasValue(now);
    assertThat(captor.getValue().getTo()).hasValue(now);
  }

  @Test
  public void testRunWithMultiProcessAndSymbols_shouldRunForEachProcessAndSymbol() {
    final ArgumentCaptor<CalcServiceRequest> captor = ArgumentCaptor.forClass(CalcServiceRequest.class);

    target("/task/calc").request().post(Entity.form(new Form()
        .param("process", "process1")
        .param("process", "process2")
        .param("symbol", "MEG")
        .param("symbol", "BDO")
    ));

    verify(calcService).calc(captor.capture());
    assertThat(captor.getValue().getProcessNames()).containsExactly("process1", "process2");
    assertThat(captor.getValue().getSymbols()).containsExactly("MEG", "BDO");
    assertThat(captor.getValue().isAllProcesses()).isFalse();
    assertThat(captor.getValue().isAllSymbols()).isFalse();
  }

  @Test
  public void testRunWithAllProcess_shouldRunAllProcesses() throws Exception {
    final ArgumentCaptor<CalcServiceRequest> captor = ArgumentCaptor.forClass(CalcServiceRequest.class);

    target("/task/calc").request().post(Entity.form(new Form()
        .param("process", "process1")
        .param("process", "all")
    ));

    verify(calcService).calc(captor.capture());
    assertThat(captor.getValue().getProcessNames()).isEmpty();
    assertThat(captor.getValue().isAllProcesses()).isTrue();
  }

  @Test
  public void testRunWithNoInputProcess_shouldRunAllProcesses() throws Exception {
    final ArgumentCaptor<CalcServiceRequest> captor = ArgumentCaptor.forClass(CalcServiceRequest.class);

    target("/task/calc").request().post(Entity.form(new Form()));

    verify(calcService).calc(captor.capture());
    assertThat(captor.getValue().getProcessNames()).isEmpty();
    assertThat(captor.getValue().getSymbols()).isEmpty();
    assertThat(captor.getValue().isAllProcesses()).isTrue();
    assertThat(captor.getValue().isAllSymbols()).isTrue();
  }

  @Test
  public void testRunWithDateRange_shouldIncludeFromToDates() throws Exception {
    final ArgumentCaptor<CalcServiceRequest> captor = ArgumentCaptor.forClass(CalcServiceRequest.class);

    target("/task/calc").request().post(Entity.form(new Form()
        .param("process", "process1")
        .param("from", "20160101")
        .param("to", "20170202")
    ));

    verify(calcService).calc(captor.capture());
    assertThat(captor.getValue().getFrom()).hasValue(LocalDate.of(2016, 1, 1));
    assertThat(captor.getValue().getTo()).hasValue(LocalDate.of(2017, 2, 2));
  }

  @Test
  public void testCalcForAllSymbols_shouldCalcForAllSymbols() throws Exception {
    final ArgumentCaptor<CalcServiceRequest> captor = ArgumentCaptor.forClass(CalcServiceRequest.class);

    target("/task/calc").request().post(Entity.form(new Form()
        .param("process", "process1")
    ));

    // Verify all symbols submitted for processing
    verify(calcService).calc(captor.capture());
    assertThat(captor.getValue().getSymbols()).isEmpty();
    assertThat(captor.getValue().isAllSymbols()).isTrue();
  }
}
