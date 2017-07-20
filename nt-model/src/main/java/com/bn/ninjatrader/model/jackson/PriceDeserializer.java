package com.bn.ninjatrader.model.jackson;

import com.bn.ninjatrader.model.entity.Price;
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

import static com.bn.ninjatrader.model.jackson.PriceAttributes.CHANGE;
import static com.bn.ninjatrader.model.jackson.PriceAttributes.CLOSE;
import static com.bn.ninjatrader.model.jackson.PriceAttributes.DATE;
import static com.bn.ninjatrader.model.jackson.PriceAttributes.HIGH;
import static com.bn.ninjatrader.model.jackson.PriceAttributes.LOW;
import static com.bn.ninjatrader.model.jackson.PriceAttributes.OPEN;
import static com.bn.ninjatrader.model.jackson.PriceAttributes.VOLUME;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceDeserializer extends StdDeserializer<Price> {
  private static final Logger LOG = LoggerFactory.getLogger(PriceDeserializer.class);

  @Inject
  public PriceDeserializer() {
    super((Class)null);
  }

  @Override
  public Price deserialize(final JsonParser jp,
                           final DeserializationContext deserializationContext)
      throws IOException {
    final JsonNode node = jp.getCodec().readTree(jp);
    return Price.builder()
        .date(LocalDate.parse(node.get(DATE).asText(), DateTimeFormatter.BASIC_ISO_DATE))
        .open(node.get(OPEN).asDouble())
        .high(node.get(HIGH).asDouble())
        .low(node.get(LOW).asDouble())
        .close(node.get(CLOSE).asDouble())
        .change(node.get(CHANGE).asDouble())
        .volume(node.get(VOLUME).asLong())
        .build();
  }
}
