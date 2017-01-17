package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.type.TimeFrame;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.Optional;


/**
 * @author bradwee2000@gmail.com
 */
@Path("/hello")
@Produces(MediaType.TEXT_HTML)
public class HelloResource {

  @GET
  public Response sayHello(@QueryParam("name") String name) {
    if (StringUtils.isEmpty(name)) {
      name = "World";
    }
    final String value = String.format("Hello, %s!!", name);
    return Response.ok(value).build();
  }

  @GET
  @Path("/time/{symbol}")
  public Response sayTime(@BeanParam Aaa findRequest) {
    return Response.ok(findRequest.toString()).build();
  }

  public static class Aaa {
    @PathParam("symbol")
    private String symbol;

    @QueryParam("period")
    private int period;
    private LocalDate from;
    private TimeFrame timeFrame;

    public Aaa(@QueryParam("from") Optional<LocalDate> from,
               @QueryParam("timeframe") TimeFrame timeFrame) {
      this.from = from.orElse(LocalDate.now());
      this.timeFrame = timeFrame;
    }

    public String getSymbol() {
      return symbol;
    }

    public int getPeriod() {
      return period;
    }

    public void setPeriod(int period) {
      this.period = period;
    }

    public LocalDate getFrom() {
      return from;
    }

    public void setFrom(LocalDate from) {
      this.from = from;
    }

    public TimeFrame getTimeFrame() {
      return timeFrame;
    }

    public void setTimeFrame(TimeFrame timeFrame) {
      this.timeFrame = timeFrame;
    }

    @Override
    public String toString() {
      return "LALALA " + symbol + " - " + from + " timeFrame:" + timeFrame;
    }
  }
}
