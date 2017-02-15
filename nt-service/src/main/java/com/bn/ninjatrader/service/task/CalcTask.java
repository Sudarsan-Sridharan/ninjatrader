package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.process.request.CalcServiceRequest;
import com.bn.ninjatrader.process.service.CalcService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * By default, executes all calculations for today
 * curl -X POST localhost:8080/tasks/calc
 *
 * To execute all calculations for specific dates:
 * curl -X POST localhost:8080/tasks/calc -d "name=all&from=19900101&to=20200101"
 *
 * To execute multiple specific calculations:
 * curl -X POST localhost:8080/tasks/calc -d "name=rsi&name=mean&from=19900101&to=20200101"
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/task/calc")
public class CalcTask {
  private static final Logger LOG = LoggerFactory.getLogger(CalcTask.class);

  private final Clock clock;
  private final CalcService calcService;

  @Inject
  public CalcTask(final Clock clock,
                  final CalcService calcService) {
    this.clock = clock;
    this.calcService = calcService;
  }

  @POST
  public Response calc(@FormParam("process") final List<String> processNames,
                   @FormParam("symbol") final List<String> inputSymbols,
                   @FormParam("from") final String fromIsoBasicDate,
                   @FormParam("to") final String toIsoBasicDate) {
    checkNotNull(processNames, "processNames must not be null.");
    checkNotNull(inputSymbols, "inputSymbols must not be null.");

    final LocalDate from = parseOrDefaultDate(fromIsoBasicDate);
    final LocalDate to = parseOrDefaultDate(toIsoBasicDate);
    final CalcServiceRequest.Builder reqBuilder = CalcServiceRequest.builder().from(from).to(to);

    // Create list of processes to run.
    if (processNames.isEmpty() || processNames.contains("all")) {
      reqBuilder.allProcesses();
    } else {
      reqBuilder.addProcessNames(processNames);
    }

    // Create list of symbols to calc.
    if (inputSymbols.isEmpty()) {
      reqBuilder.allSymbols();
    } else {
      reqBuilder.addSymbols(inputSymbols);
    }

    calcService.calc(reqBuilder.build());

    return Response.noContent().build();
  }

  private LocalDate parseOrDefaultDate(final String isoBasicDate) {
    return isoBasicDate == null ?
        DateUtil.phNow(clock) :
        LocalDate.parse(isoBasicDate, DateTimeFormatter.BASIC_ISO_DATE);
  }
}
