package com.bn.ninjatrader.simulation.script;

import com.bn.ninjatrader.model.entity.TradeAlgorithm;

import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AlgorithmScriptFactory {

    public AlgorithmScript create(final TradeAlgorithm algorithm) {
        return new GroovyAlgorithmScript(algorithm.getAlgorithm());
    }
}
