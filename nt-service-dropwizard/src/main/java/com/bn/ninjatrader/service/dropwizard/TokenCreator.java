package com.bn.ninjatrader.service.dropwizard;

import com.bn.ninjatrader.auth.guice.NtAuthModule;
import com.bn.ninjatrader.auth.token.TokenGenerator;
import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.scheduler.guice.NtSchedulerModule;
import com.bn.ninjatrader.service.guice.NtServiceModule;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class TokenCreator {
    private static final Logger LOG = LoggerFactory.getLogger(TokenCreator.class);

    public static void main(String args[]) throws IOException {
        final Injector injector = Guice.createInjector(
            new NtAuthModule(),
            new NtModelMongoModule(),
            new NtSimulationModule(),
            new NtSchedulerModule(),
            new NtServiceModule()
        );

        final TokenGenerator tokenGenerator = injector.getInstance(TokenGenerator.class);

        final UserDao userDao = injector.getInstance(UserDao.class);

//        final String res = Request.Get("https://ph15.colfinancial.com/ape/FINAL2_STARTER/quotes/Pse_Quote_2_DB_D.asp?q=WLCON")
//            .addHeader("Cookie", "ASPSESSIONIDCEQRQSCR=KIKFOEHABNJIJFKDODMNLGAL").execute().returnContent().asString();
//
//        LOG.info("RESPONSE: {}", res);

        final String res = Request.Get("http://pse.tools").execute().returnContent().asString();

        final Elements rows = Jsoup.parse(res).body()
            .getElementsByClass("table-condensed").get(0)
            .getElementsByTag("tbody").get(0)
            .children();

        Iterator<Element> iter = rows.iterator();
        while(iter.hasNext()) {
            Element e = iter.next();
            LOG.info("{}", e.attr("id"));
        }

//        userDao.saveUser(User.builder().userId("admin")
//            .firstname("admin")
//            .lastname("admin")
//            .addRoles(Role.ADMIN.getId()).build());

//        final String token = tokenGenerator.createTokenForUserId("admin");
//        LOG.info("TOKEN: {}", token);
    }
}
