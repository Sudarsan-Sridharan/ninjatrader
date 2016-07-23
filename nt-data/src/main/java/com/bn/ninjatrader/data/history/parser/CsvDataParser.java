package com.bn.ninjatrader.data.history.parser;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Created by Brad on 4/27/16.
 */
@Singleton
public class CsvDataParser implements DataParser<File> {

  private static final Logger log = LoggerFactory.getLogger(CsvDataParser.class);
  private static final Charset CHARSET = Charset.forName("UTF-8");

  private static final DateTimeFormatter[] DATE_FORMATS =
          new DateTimeFormatter[] {
                  DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                  DateTimeFormatter.ofPattern("M/d/yy"),
                  DateTimeFormatter.ofPattern("M/dd/yy"),
                  DateTimeFormatter.ofPattern("M/d/yyyy"),
                  DateTimeFormatter.ofPattern("M/dd/yyyy"),
                  DateTimeFormatter.ofPattern("MM-dd-yyyy")};

  public List<DailyQuote> parse(File file) throws Exception {
    List<DailyQuote> quotes = Lists.newArrayList();
    List<String> rows = FileUtils.readLines(file, CHARSET);
    LocalDate date = null;

    for (String row : rows) {
      try {
        String cols[] = row.split(",");
        String symbol = cols[0];


        if (date == null) {
          try {
            date = parseDate(cols[1]);
          } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Error parsing row: {} on file: {}", row, file.getAbsolutePath());
            throw e;
          }
        }

        double open = Double.parseDouble(cols[2]);
        double high = Double.parseDouble(cols[3]);
        double low = Double.parseDouble(cols[4]);
        double close = Double.parseDouble(cols[5]);
        long volume = Long.parseLong(cols[6]);

        DailyQuote dq = new DailyQuote(symbol, date, open, high, low, close, volume);
        quotes.add(dq);

      } catch (DateTimeParseException e) {
        log.error("Error parsing date in file: {}", file.getAbsolutePath());
        log.error(e.getMessage());
        log.error("Caused by row: {}", row);
      } catch (NumberFormatException e) {
        log.error("Error number in file: {}", file.getAbsolutePath());
        log.error(e.getMessage());
        log.error("Caused by row: {}", row);
      }
    }
    return quotes;
  }

  private LocalDate parseDate(String date) {
    for (DateTimeFormatter dtf : DATE_FORMATS) {
      try {
        return LocalDate.parse(date, dtf);
      } catch (DateTimeParseException e) {
        continue;
      }
    }
    throw new DateTimeParseException("Unable to parse " + date, date, 0);
  }
}
