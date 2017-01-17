package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.guice.NtModelTestModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 7/28/16.
 */
public class ReportDaoTest extends AbstractDaoTest {

  private static final Logger LOG = LoggerFactory.getLogger(ReportDaoTest.class);

  private ReportDao reportDao;

  @BeforeClass
  public void setup() {
    Injector injector = Guice.createInjector(new NtModelTestModule());
    reportDao = injector.getInstance(ReportDao.class);
  }

  @BeforeMethod
  public void cleanup() {
    MongoCollection collection = reportDao.getMongoCollection();
    collection.remove();
  }

  @Test
  public void testSaveAndFind_shouldSaveAndReturnSameReport() {
    Report report = new Report();
    report.setReportId("REPORT_ID");
    report.setUser("test_user");
    report.setData("Sample Data");

    reportDao.save(report);

    Optional<Report> foundReport = reportDao.findByReportId(report.getReportId());

    assertThat(foundReport.isPresent()).isTrue();
    assertThat(foundReport.get()).isEqualTo(report);
  }

  @Test
  public void testSaveAndFindWithNoPriorReportId_shouldHaveReportIdAssigned() {
    Report report = new Report();
    report.setUser("test_user");
    report.setData("Sample Data");

    report = reportDao.save(report);

    assertThat(report.getReportId()).isNotEmpty();

    Optional<Report> foundReport = reportDao.findByReportId(report.getReportId());

    assertThat(foundReport.isPresent()).isTrue();
    assertThat(foundReport.get()).isEqualTo(report);
  }

  @Test
  public void testFindNonExisting_shouldReturnOptionalEmpty() {
    Optional<Report> foundReport = reportDao.findByReportId("NON_EXISTING_SETTING");
    assertThat(foundReport.isPresent()).isFalse();
  }

  @Test
  public void testSaveWithSameReportId_shouldOverwrite() {
    Report oldReport = new Report();
    oldReport.setReportId("report_id");
    oldReport.setData("Old Data");

    Report newReport = new Report();
    newReport.setReportId("report_id");
    newReport.setData("New Data");

    reportDao.save(oldReport);
    reportDao.save(newReport);

    Optional<Report> foundReport = reportDao.findByReportId(oldReport.getReportId());
    assertThat(foundReport.isPresent()).isTrue();
    assertThat(foundReport.get()).isEqualTo(newReport);
  }
}
