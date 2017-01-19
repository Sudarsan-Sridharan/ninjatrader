package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.annotation.ReportCollection;
import com.bn.ninjatrader.model.document.ReportDocument;
import com.bn.ninjatrader.model.util.IdGenerator;
import com.bn.ninjatrader.model.util.Queries;
import com.bn.ninjatrader.model.util.QueryParam;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by Brad on 7/28/16.
 */
@Singleton
public class ReportDao extends AbstractDao<ReportDocument> {
  private static final Logger LOG = LoggerFactory.getLogger(ReportDao.class);

  @Inject
  private IdGenerator idGenerator;

  @Inject
  public ReportDao(@ReportCollection final MongoCollection mongoCollection) {
    super(mongoCollection);
    mongoCollection.ensureIndex(
        String.format("{%s.%s : 1}", QueryParam.DATA, QueryParam.REPORT_ID), "{unique: true}");
  }

  public Optional<Report> findByReportId(final String reportId) {
    final ReportDocument reportDocument = getMongoCollection().findOne(Queries.FIND_BY_REPORT_ID, reportId)
        .as(ReportDocument.class);
    if (reportDocument == null) {
      return Optional.empty();
    }
    return Optional.of(reportDocument.getReport());
  }

  public Report save(final Report report) {
    if (StringUtils.isEmpty(report.getReportId())) {
      report.setReportId(idGenerator.createId());
    }

    final ReportDocument reportDocument = new ReportDocument();
    reportDocument.setReport(report);

    report.setLastUpdateDate(LocalDateTime.now());

    getMongoCollection().update(Queries.FIND_BY_REPORT_ID, report.getReportId()).upsert().with(reportDocument);

    return report;
  }
}
