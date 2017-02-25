package com.bn.ninjatrader.model.datastore.dao;

import com.bn.ninjatrader.model.deprecated.Report;
import com.bn.ninjatrader.model.util.ObjectMapperProvider;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.model.datastore.document.ReportDocument;
import com.bn.ninjatrader.model.request.FindReportRequest;
import com.bn.ninjatrader.model.request.SaveReportRequest;
import com.bn.ninjatrader.model.util.IdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ReportDaoDatastore implements ReportDao {

  private final IdGenerator idGenerator;
  private final ObjectMapper om;

  @Inject
  public ReportDaoDatastore(final IdGenerator idGenerator,
                            final ObjectMapperProvider objectMapperProvider) {
    this.idGenerator = idGenerator;
    this.om = objectMapperProvider.get();
  }

  @Override
  public List<Report> save(final SaveReportRequest request) {
    final List<ReportDocument> documents = Lists.newArrayList();
    try {
      for (final Report report : request.getReports()) {
        if (StringUtils.isEmpty(report.getReportId())) {
          report.setReportId(idGenerator.createId());
        }
        final String jsonReport = om.writeValueAsString(report.getData());
        documents.add(new ReportDocument(report.getUserId(), report.getReportId(), jsonReport));
      }
      ofy().save().entities(documents).now();
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
    return request.getReports();
  }

  @Override
  public List<Report> find(final FindReportRequest request) {
    final List<ReportDocument> documents = ofy().load().type(ReportDocument.class)
        .filter("reportId = ", request.getReportId().get())
        .list();
    return documents.stream().map(doc -> new Report(doc.getReportId(), doc.getUserId(), doc.getData().getValue()))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Report> findOne(final FindReportRequest request) {
    final List<Report> reports = find(request);
    return reports.isEmpty() ? Optional.empty() : Optional.ofNullable(reports.get(0));
  }
}
