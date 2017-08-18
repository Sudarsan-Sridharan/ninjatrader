package com.bn.ninjatrader.thirdparty.psetrader;

import com.bn.ninjatrader.common.model.DailyQuote;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PseTraderService {
  private static final Logger LOG = LoggerFactory.getLogger(PseTraderService.class);
  private static final String URL = "https://pse-trader.appspot.com/api/quote/";
  private static final ObjectMapper om = new ObjectMapper()
      .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public List<DailyQuote> getDailyQuotes(final LocalDate date) {
    checkNotNull(date, "date must not be null.");
    final String basicIsoDate = date.format(DateTimeFormatter.BASIC_ISO_DATE);
    try {
      final String json = Request.Get(URL + basicIsoDate)
          .connectTimeout(10000)
          .socketTimeout(10000)
          .execute()
          .returnContent().asString();

      final List<PseTraderQuote> pseTraderQuotes =
          Lists.newArrayList(om.readValue(json, PseTraderQuote[].class));

      return pseTraderQuotes.stream()
          .map(pseTraderQuote -> {
            pseTraderQuote.setDate(date);
            return pseTraderQuote.getDailyQuote();
          })
          .collect(Collectors.toList());
    } catch (final HttpResponseException e) {
      if (e.getStatusCode() == 404) {
        LOG.warn("No quotes found for {}", date);
        return Collections.emptyList();
      }
      throw new RuntimeException(e);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String args[]) {
    PseTraderService service = new PseTraderService();
    List<DailyQuote> dailyQuotes = service.getDailyQuotes(LocalDate.of(2016, 12, 16));

    for (DailyQuote quote : dailyQuotes) {
      LOG.info("{}", quote);
    }
  }
}
