package com.bn.ninjatrader.thirdparty.util;

import com.google.inject.Singleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Brad on 7/26/16.
 */
@Singleton
public class DocumentDownloader {

  public Document getDocumentFromUrl(String url) {
    try {
      return Jsoup.connect(url).timeout(15000).get();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
