package com.bn.ninjatrader.thirdparty.investagrams;

import com.bn.ninjatrader.thirdparty.util.DocumentDownloader;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Brad on 7/26/16.
 */
public class DailyPriceDownloaderTest {

  private static final Logger log = LoggerFactory.getLogger(DailyPriceDownloaderTest.class);
  private static final Charset CHARSET = Charset.forName("UTF-8");
  private static final String TEST_FILE_TO_PARSE = "/investagrams_daily.html";

  @Injectable
  private DocumentDownloader documentDownloader;

  @Tested
  private DailyPriceDownloader dailyPriceDownloader;

  @BeforeMethod
  public void setup() throws IOException {
    String html = IOUtils.toString(getClass().getResourceAsStream(TEST_FILE_TO_PARSE), CHARSET);
    Document doc = Jsoup.parse(html);

    new Expectations() {{
      documentDownloader.getDocumentFromUrl(anyString);
      result = doc;
    }};
  }

//  @Test
//  private void testDownload() {
//    List<DailyQuote> result = dailyPriceDownloader.download();
//    assertEquals(result.size(), 8);
//
//    LocalDate today = LocalDate.now();
//
//    TestUtil.assertDailyQuoteEquals(result.get(0), new DailyQuote("2GO", today, 7.40, 7.40, 7.33, 7.40, 72500));
//    TestUtil.assertDailyQuoteEquals(result.get(1), new DailyQuote("ABA", today, 0.39, 0.40, 0.39, 0.40, 190000));
//    TestUtil.assertDailyQuoteEquals(result.get(2), new DailyQuote("ABG", today, 13.50, 13.50, 12.90, 13.5, 19000));
//    TestUtil.assertDailyQuoteEquals(result.get(7), new DailyQuote("ACPB1", today, 520, 520, 520, 520, 1500));
//  }
}
