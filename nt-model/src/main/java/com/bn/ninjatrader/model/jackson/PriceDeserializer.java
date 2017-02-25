package com.bn.ninjatrader.model.jackson;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceDeserializer extends StdDeserializer<Price> {
  private static final Logger LOG = LoggerFactory.getLogger(PriceDeserializer.class);

  private final PriceBuilderFactory pbf;

  @Inject
  public PriceDeserializer(final PriceBuilderFactory pbf) {
    super((Class)null);
    this.pbf = pbf;
  }

  @Override
  public Price deserialize(final JsonParser jp,
                           final DeserializationContext deserializationContext)
      throws IOException {
    final JsonNode node = jp.getCodec().readTree(jp);
    return pbf.builder()
        .date(LocalDate.parse(node.get("d").asText(), DateTimeFormatter.BASIC_ISO_DATE))
        .open(node.get("o").asDouble())
        .high(node.get("h").asDouble())
        .low(node.get("l").asDouble())
        .close(node.get("c").asDouble())
        .change(node.get("ch").asDouble())
        .volume(node.get("v").asLong())
        .build();
  }
}
