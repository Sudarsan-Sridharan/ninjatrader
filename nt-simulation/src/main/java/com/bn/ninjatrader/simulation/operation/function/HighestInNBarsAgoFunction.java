//package com.bn.ninjatrader.simulation.operation.function;
//
//import com.bn.ninjatrader.simulation.data.BarData;
//import com.bn.ninjatrader.simulation.operation.Operation;
//import com.bn.ninjatrader.simulation.operation.Variable;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Optional;
//import java.util.Set;
//
///**
// * Created by Brad on 8/29/16.
// */
//public class HighestInNBarsAgoFunction implements Operation {
//  private static final Logger LOG = LoggerFactory.getLogger(HighestInNBarsAgoFunction.class);
//
//  public static final HighestInNBarsAgoFunction of(final Operation operation) {
//    return new HighestInNBarsAgoFunction(operation);
//  }
//
//  @JsonProperty("numOfBarsAgo")
//  private int numOfBarsAgo;
//
//  @JsonProperty("operation")
//  private final Operation operation;
//
//  public HighestInNBarsAgoFunction(final Operation operation) {
//    this(operation, 0);
//  }
//
//  public HighestInNBarsAgoFunction(@JsonProperty("operation") final Operation operation,
//                                   @JsonProperty("numOfBarsAgo") final int numOfBarsAgo) {
//    this.numOfBarsAgo = numOfBarsAgo;
//    this.operation = operation;
//  }
//
//  @Override
//  public double getValue(final BarData barData) {
//    double highestValue = 0;
//    for (int i=1; i <= numOfBarsAgo; i++) {
//      final Optional<BarData> pastBarData = barData.getNBarsAgo(i);
//      if (pastBarData.isPresent()) {
//        highestValue = Math.max(operation.getValue(pastBarData.get()), highestValue);
//      } else {
//        return highestValue;
//      }
//    }
//    return highestValue;
//  }
//
//  @Override
//  public Set<Variable> getVariables() {
//    return operation.getVariables();
//  }
//
//  public int getNumOfBarsAgo() {
//    return numOfBarsAgo;
//  }
//
//  public HighestInNBarsAgoFunction withinBarsAgo(final int numOfBarsAgo) {
//    this.numOfBarsAgo = numOfBarsAgo;
//    return this;
//  }
//
//  public Operation getOperation() {
//    return operation;
//  }
//}
