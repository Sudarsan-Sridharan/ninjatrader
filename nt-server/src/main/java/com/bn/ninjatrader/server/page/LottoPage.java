package com.bn.ninjatrader.server.page;

import com.bn.ninjatrader.data.lotto.LottoService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by a- on 6/23/16.
 */
@Singleton
public class LottoPage extends HttpServlet {

  @Inject
  private LottoService lottoService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    VelocityContext context = new VelocityContext();

    context.put("contextPath", req.getContextPath());

//    List<Data> dataList = lottoService.readData();

//    context.put("dataList", dataList);

    Velocity.mergeTemplate("velocity/pages/lotto.vm", "UTF-8", context, resp.getWriter());
  }



}
