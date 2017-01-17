package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 6/8/16.
 */
public class SequentialCalcProcess implements CalcProcess, Collection<CalcProcess> {
  private static final Logger LOG = LoggerFactory.getLogger(SequentialCalcProcess.class);
  private static final String NULL_PROCESS_ERROR_MSG = "Cannot insert a null process";

  public static final SequentialCalcProcess newInstance(final String processName,
                                                        final CalcProcess calcProcess,
                                                        final CalcProcess ... moreProcesses) {
    return new SequentialCalcProcess(processName, calcProcess, moreProcesses);
  }

  private final String processName;
  private final List<CalcProcess> processList;

  public SequentialCalcProcess(final String processName,
                               final CalcProcess calcProcess,
                               final CalcProcess ... moreCalcProcesses) {
    checkNotNull(calcProcess, NULL_PROCESS_ERROR_MSG);
    for (final CalcProcess process : moreCalcProcesses) {
      checkNotNull(process, NULL_PROCESS_ERROR_MSG);
    }
    this.processName = processName;
    this.processList = Lists.asList(calcProcess, moreCalcProcesses);
  }

  @Override
  public void process(final CalcRequest calcRequest) {
    for (final CalcProcess process : processList) {
      process.process(calcRequest);
    }
  }

  @Override
  public String getProcessName() {
    return processName;
  }
  @Override
  public int size() {
    return processList.size();
  }
  @Override
  public boolean isEmpty() {
    return processList.isEmpty();
  }
  @Override
  public boolean contains(final Object o) {
    return processList.contains(o);
  }
  @Override
  public Iterator<CalcProcess> iterator() {
    return processList.iterator();
  }
  @Override
  public Object[] toArray() {
    return processList.toArray();
  }
  @Override
  public <T> T[] toArray(final T[] a) {
    return processList.toArray(a);
  }
  @Override
  public boolean add(final CalcProcess calcProcess) {
    return processList.add(calcProcess);
  }
  @Override
  public boolean remove(final Object o) {
    return processList.remove(o);
  }
  @Override
  public boolean containsAll(final Collection<?> c) {
    return processList.containsAll(c);
  }
  @Override
  public boolean addAll(final Collection<? extends CalcProcess> c) {
    return processList.addAll(c);
  }
  @Override
  public boolean removeAll(final Collection<?> c) {
    return processList.removeAll(c);
  }
  @Override
  public boolean retainAll(final Collection<?> c) {
    return retainAll(c);
  }
  @Override
  public void clear() {
    processList.clear();
  }
}
