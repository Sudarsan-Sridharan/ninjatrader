package com.bn.ninjatrader.data.lotto;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brad on 6/23/16.
 */
@Singleton
public class LottoService {

  private static final Logger log = LoggerFactory.getLogger(LottoService.class);

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yy");
  private static final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("M/d/yyyy");
  private static DecimalFormat df2 = new DecimalFormat(".000");

  public static List<Integer> highlightWins = Lists.newArrayList();

  public List<Data> readData(LottoType lottoType, Period period) throws IOException {
    BufferedReader br = new BufferedReader(
        new FileReader(
        new File(lottoType.path())));

    String line = null;
    List<Win> wins = Lists.newArrayList();
    while ((line = br.readLine()) != null) {
      wins.add(parseString(line));
    }

    LocalDate date = LocalDate.now();
    List<Data> dataList = Lists.newArrayList();
    Data data = new Data(lottoType.max());
    dataList.add(data);
    for (Win win : wins) {
      if (period == Period.YEARLY && date.getYear() != win.getDate().getYear()) {
        data = new Data(lottoType.max());
        data.setDate(win.getDate());
        dataList.add(data);
        date = win.getDate();
      }

      for (int num : win.getNums()) {
        data.getCount()[num]++;
      }
    }

    br.close();

    for (Data data1 : dataList) {
      printSortedStats(data1);
    }

    return dataList;
  }

  public void printSortedStats(Data data) {
    int[] countCopy = Arrays.copyOfRange(data.getCount(), 1, data.getCount().length);
    Arrays.sort(countCopy);

    int totalSum = 0;
    for(int i : countCopy) {
      totalSum += i;
    }

    log.info("Top Losers");
    int prevCount = -1;
    for (int count : countCopy) {
      if (prevCount == count) continue;
      prevCount = count;

      StringBuilder sb = new StringBuilder();

      for (int lottoNum = 1; lottoNum < data.getCount().length; lottoNum++) {
        if (data.getCount()[lottoNum] == count) {
          if (highlightWins.contains(lottoNum)) {
            sb.append("[").append(lottoNum).append("]").append(" ");
          } else {
            sb.append(lottoNum).append(" ");
          }
        }
      }

      double chance = (double)count / (double)totalSum * 100;

      log.info("Wins: {}   Chance: {}   Nums: {}", count, df2.format(chance), sb.toString());
    }
  }

  public Win parseString(String line) {
    String tuple[] = line.split(",");
    String lottoNumbers = tuple[0];
    String lottoDrawDate = tuple[1];
    int nums[] = parseLottoNumbers(lottoNumbers);
    LocalDate date;
    try {
      date = LocalDate.parse(lottoDrawDate, dtf);
    } catch(DateTimeParseException e) {
      date = LocalDate.parse(lottoDrawDate, dtf2);
    }
    return new Win(date, nums);
  }

  public static int[] parseLottoNumbers(String lottoNumbers) {
    String values[] = lottoNumbers.replaceAll(" ", "").split("-");
    int nums[] = new int[values.length];
    for (int i = 0; i < values.length; i++) {
      nums[i] = Integer.parseInt(values[i]);
    }
    return nums;
  }

  public static void main(String args[]) throws IOException {
    LottoService app = new LottoService();
    LottoService.highlightWins.addAll(Ints.asList(parseLottoNumbers("05-27-21-45-02-47")));
//    LottoService.highlightWins.clear();
    app.readData(LottoType.LOTTO55, Period.ALLTIME);
  }
}