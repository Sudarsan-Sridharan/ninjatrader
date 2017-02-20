//package com.bn.ninjatrader.service.appengine.migrate;
//
//import com.bn.ninjatrader.common.data.Price;
//import com.bn.ninjatrader.common.type.TimeFrame;
//import com.bn.ninjatrader.model.appengine.PriceDocument;
//import com.bn.ninjatrader.model.appengine.dao.PriceDaoGae;
//import com.bn.ninjatrader.model.dao.PriceDao;
//import com.bn.ninjatrader.model.guice.NtModelModule;
//import com.bn.ninjatrader.model.request.FindRequest;
//import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
//import com.google.appengine.tools.remoteapi.RemoteApiOptions;
//import com.google.common.collect.Lists;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import com.googlecode.objectify.Key;
//import com.googlecode.objectify.ObjectifyService;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author bradwee2000@gmail.com
// */
//@WebServlet(name="dataMigrator", value="/migrate")
//public class DataMigrator extends HttpServlet {
//  private static final Logger LOG = LoggerFactory.getLogger(DataMigrator.class);
//
//  private static Injector injector;
//  private static PriceDao priceDao;
//  private static PriceDaoGae priceDaoGae;
//
//  public DataMigrator() {
//    injector = Guice.createInjector(new NtModelModule());
//    priceDao = injector.getInstance(PriceDao.class);
//    priceDaoGae = injector.getInstance(PriceDaoGae.class);
//  }
//
//  @Override
//  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//    LOG.info("Getting prices from Le mongo");
//
//    final String symbol = req.getParameter("symbol");
//    final int year = Integer.parseInt(req.getParameter("year"));
//
//
//    final List<String> symbols;
//    if (StringUtils.isEmpty(symbol)) {
//      symbols = Lists.newArrayList(priceDao.findAllSymbols());
//    } else {
//      symbols = Lists.newArrayList(symbol);
//    }
//    Collections.sort(symbols);
//
//    final List<PriceDocument> documents = Lists.newArrayList();
//    for (final String sym : symbols) {
////      if (Lists.newArrayList('A', 'B','C','D','E','F','2','G','H','I','J','K','L','M','N','O').contains(sym.charAt(0))) {
////        continue;
////      }
//
//      final List<Price> prices = priceDao.find(FindRequest.findSymbol(sym)
//          .from(LocalDate.of(year, 1, 1)).to(LocalDate.of(year, 12, 31)));
//
//      if (prices.isEmpty()) {
//        continue;
//      }
//
//      LOG.info("{} -- Got {} prices from mongo", sym, prices.size());
//
//      final PriceDocument priceDocument = new PriceDocument(sym, year, TimeFrame.ONE_DAY);
//      priceDocument.setData(prices);
//      documents.add(priceDocument);
//
//      if (documents.size() % 10 == 0) {
//        LOG.info("=========== Uploading.. ");
//        upload(documents);
//        documents.clear();
//      }
//    }
//    LOG.info("Saving {} documents", documents.size());
//
//
//    upload(documents);
//  }
//
//  private void upload(final List<PriceDocument> documents) throws IOException {
//    final RemoteApiOptions options = new RemoteApiOptions().server("remote-dot-beachninjatrader.appspot.com",
//        443).useApplicationDefaultCredential();
//
//    final RemoteApiInstaller installer = new RemoteApiInstaller();
//    installer.install(options);
//
//    try {
//      final Map<Key<PriceDocument>, PriceDocument> result =
//          ObjectifyService.ofy().save().entities(documents).now();
//      LOG.info("DONE!");
//    } finally {
//      installer.uninstall();
//    }
//  }
//}
