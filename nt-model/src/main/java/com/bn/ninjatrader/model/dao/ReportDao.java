package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.request.FindReportRequest;
import com.bn.ninjatrader.model.request.SaveReportRequest;

import java.util.List;
import java.util.Optional;

/**
 * Created by Brad on 7/28/16.
 */
public interface ReportDao {

  List<Report> save(final SaveReportRequest request);

  List<Report> find(final FindReportRequest request);

  Optional<Report> findOne(final FindReportRequest request);
}
