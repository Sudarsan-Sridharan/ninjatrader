package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.process.adjustment.PriceAdjustmentRequest;
import com.bn.ninjatrader.process.adjustment.PriceAdjustmentService;
import com.bn.ninjatrader.service.exception.Exceptions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Path("/task/price-adjustment")
public class PriceAdjustmentTask {
  private static final Logger LOG = LoggerFactory.getLogger(PriceAdjustmentTask.class);
  public static final String ERROR_SYM_PARAM_REQUIRED = "\"symbol\" parameter is required.";
  public static final String ERROR_SCRIPT_PARAM_REQUIRED = "\"script\" parameter is required.";
  public static final String ERROR_FROM_PARAM_REQUIRED = "\"from\" parameter is required.";
  public static final String ERROR_TO_PARAM_REQUIRED = "\"to\" parameter is required.";

  private final PriceAdjustmentService priceAdjustmentService;

  @Inject
  public PriceAdjustmentTask(final PriceAdjustmentService priceAdjustmentService) {
    this.priceAdjustmentService = priceAdjustmentService;
  }

  @POST
  @Path("/run")
  public Response run(final PriceAdjustmentRequest request) {
    if (StringUtils.isEmpty(request.getSymbol())) {
      Exceptions.throwBadRequest(ERROR_SYM_PARAM_REQUIRED);
    }
    if (StringUtils.isEmpty(request.getScript())) {
      Exceptions.throwBadRequest(ERROR_SCRIPT_PARAM_REQUIRED);
    }
    if (request.getFrom() == null) {
      Exceptions.throwBadRequest(ERROR_FROM_PARAM_REQUIRED);
    }
    if (request.getTo() == null) {
      Exceptions.throwBadRequest(ERROR_TO_PARAM_REQUIRED);
    }

    priceAdjustmentService.adjustPrices(request);

    return Response.ok().build();
  }
}
