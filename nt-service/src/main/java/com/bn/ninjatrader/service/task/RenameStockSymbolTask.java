package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.service.exception.Exceptions;
import com.bn.ninjatrader.service.model.RenameStockSymbolRequest;
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
@Path("/tasks/rename-stock-symbol")
public class RenameStockSymbolTask {
  private static final Logger LOG = LoggerFactory.getLogger(RenameStockSymbolTask.class);
  public static final String ERROR_FROM_PARAM_REQUIRED = "\"from\" parameter is required.";
  public static final String ERROR_TO_PARAM_REQUIRED = "\"to\" parameter is required.";

  private final PriceDao priceDao;

  @Inject
  public RenameStockSymbolTask(final PriceDao priceDao) {
    this.priceDao = priceDao;
  }

  @POST
  @Path("/run")
  public Response run(final RenameStockSymbolRequest request) {
    if (StringUtils.isEmpty(request.getFrom())) {
      Exceptions.throwBadRequest(ERROR_FROM_PARAM_REQUIRED);
    }
    if (StringUtils.isEmpty(request.getTo())) {
      Exceptions.throwBadRequest(ERROR_TO_PARAM_REQUIRED);
    }

    priceDao.renameSymbol(request.getFrom()).to(request.getTo()).now();

    return Response.ok().build();
  }
}
