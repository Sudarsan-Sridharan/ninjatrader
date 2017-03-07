package com.bn.ninjatrader.simulation.jackson;

import com.bn.ninjatrader.simulation.logicexpression.operation.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtSimulationModule extends SimpleModule {

  public NtSimulationModule() {
    registerSubtypes(
        new NamedType(PcntChangeValue.class, "pcntChange"),
        new NamedType(HighestValue.class, "highest"),
        new NamedType(LowestValue.class, "lowest"),
        new NamedType(PipValue.class, "pip"),
        new NamedType(PropertyValue.class, "property"),
        new NamedType(HistoryValue.class, "history")
    );
  }
}
