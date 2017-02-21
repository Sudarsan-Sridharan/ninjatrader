package com.bn.ninjatrader.model.dao.mongo;

import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.annotation.ReportCollection;
import com.bn.ninjatrader.model.dao.AbstractDao;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.model.document.ReportMongoDocument;
import com.bn.ninjatrader.model.request.FindReportRequest;
import com.bn.ninjatrader.model.request.SaveReportRequest;
import com.bn.ninjatrader.model.util.IdGenerator;
import com.bn.ninjatrader.model.util.Queries;
import com.bn.ninjatrader.model.util.QueryParam;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Brad on 7/28/16.
 */
@Singleton
public class ReportDaoMongo extends AbstractDao<ReportMongoDocument>
    implements ReportDao {
  private static final Logger LOG = LoggerFactory.getLogger(ReportDaoMongo.class);

  private final IdGenerator idGenerator;

  @Inject
  public ReportDaoMongo(@ReportCollection final MongoCollection mongoCollection,
                        final IdGenerator idGenerator) {
    super(mongoCollection);
    this.idGenerator = idGenerator;
    mongoCollection.ensureIndex(
        String.format("{%s.%s : 1}", QueryParam.DATA, QueryParam.REPORT_ID), "{unique: true}");
  }

  @Override
  public List<Report> save(final SaveReportRequest request) {
    for (final Report report : request.getReports()) {
      if (StringUtils.isEmpty(report.getReportId())) {
        report.setReportId(idGenerator.createId());
      }

      final ReportMongoDocument reportDocument = new ReportMongoDocument();
      reportDocument.setReport(report);

      report.setLastUpdateDate(LocalDateTime.now());

      getMongoCollection().update(Queries.FIND_BY_REPORT_ID, report.getReportId())
          .upsert()
          .with(reportDocument);

      getMongoCollection().find().as(ReportMongoDocument.class).forEach(reportDocument1 -> LOG.info("DDD {}", reportDocument1));
    }
    return request.getReports();
  }

  @Override
  public List<Report> find(final FindReportRequest request) {
    final List<ReportMongoDocument> documents = Lists.newArrayList(getMongoCollection()
        .find(Queries.FIND_BY_REPORT_ID, request.getReportId().get())
        .as(ReportMongoDocument.class).iterator());
    return documents.stream().map(document -> document.getReport()).collect(Collectors.toList());
  }

  @Override
  public Optional<Report> findOne(final FindReportRequest request) {
    final ReportMongoDocument document = getMongoCollection()
        .findOne(Queries.FIND_BY_REPORT_ID, request.getReportId().get())
        .as(ReportMongoDocument.class);
    if (document == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(document.getReport());
  }
}
