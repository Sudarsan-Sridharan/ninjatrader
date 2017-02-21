package com.bn.ninjatrader.model.dao.mongo;

import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.guice.NtModelTestModule;
import com.bn.ninjatrader.model.request.SaveReportRequest;
import com.bn.ninjatrader.model.util.IdGenerator;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import org.jongo.MongoCollection;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bn.ninjatrader.model.request.FindReportRequest.withReportId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Brad on 7/28/16.
 */
public class ReportDaoMongoTest {
  private static final Logger LOG = LoggerFactory.getLogger(ReportDaoMongoTest.class);

  private static IdGenerator idGenerator;
  private static Injector injector;

  private ReportDaoMongo reportDao;

  @BeforeClass
  public static void setup() {
    idGenerator = mock(IdGenerator.class);
    injector = Guice.createInjector(Modules
        .override(new NtModelTestModule())
        .with(new AbstractModule() {
          @Override
          protected void configure() {
            bind(Clock.class).toInstance(Clock.systemDefaultZone());
            bind(IdGenerator.class).toInstance(idGenerator);
          }
        })
    );
  }

  @Before
  public void before() {
    reset(idGenerator);
    when(idGenerator.createId()).thenReturn(UUID.randomUUID().toString());

    reportDao = injector.getInstance(ReportDaoMongo.class);

    final MongoCollection collection = reportDao.getMongoCollection();
    collection.remove();
  }

  @Test
  public void testSaveAndFind_shouldSaveAndReturnSameReport() {
    final Report report = new Report();
    report.setReportId("REPORT_ID");
    report.setUserId("test_user");
    report.setData("Sample Data");

    final List<Report> savedReports = reportDao.save(SaveReportRequest.save(report));
    assertThat(savedReports).hasSize(1).containsExactly(report);

    final Optional<Report> foundReport = reportDao.findOne(withReportId("REPORT_ID"));

    assertThat(foundReport.isPresent()).isTrue();
    assertThat(foundReport.get()).isEqualTo(report);
  }

  @Test
  public void testSaveAndFindWithNoPriorReportId_shouldHaveReportIdAssigned() {
    final Report report = new Report();
    report.setUserId("test_user");
    report.setData("Sample Data");

    final List<Report> savedReports = reportDao.save(SaveReportRequest.save(report));
    assertThat(savedReports).hasSize(1);
    assertThat(savedReports.get(0).getReportId()).isNotEmpty();

    final Optional<Report> foundReport = reportDao.findOne(withReportId(savedReports.get(0).getReportId()));

    assertThat(foundReport).hasValue(report);
  }

  @Test
  public void testFindNonExisting_shouldReturnOptionalEmpty() {
    final Optional<Report> foundReport = reportDao.findOne(withReportId("NON_EXISTING_SETTING"));
    assertThat(foundReport.isPresent()).isFalse();
  }

  @Test
  public void testSaveWithSameReportId_shouldOverwrite() {
    final Report oldReport = new Report();
    oldReport.setReportId("report_id");
    oldReport.setData("Old Data");

    final Report newReport = new Report();
    newReport.setReportId("report_id");
    newReport.setData("New Data");

    reportDao.save(SaveReportRequest.save(oldReport));
    reportDao.save(SaveReportRequest.save(newReport));

    final Optional<Report> foundReport = reportDao.findOne(withReportId(oldReport.getReportId()));
    assertThat(foundReport.isPresent()).isTrue();
    assertThat(foundReport.get()).isEqualTo(newReport);
  }
}
