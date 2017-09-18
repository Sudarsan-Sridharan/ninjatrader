package com.bn.ninjatrader.model.jackson;

import com.bn.ninjatrader.common.model.Price;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.inject.Singleton;

import java.io.IOException;
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
public class PriceSerializer extends StdSerializer<Price> {

  public PriceSerializer() {
    super(Price.class);
  }

  @Override
  public void serialize(final Price price,
                        final JsonGenerator gen,
                        final SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField(DATE, price.getDate().format(DateTimeFormatter.BASIC_ISO_DATE));
    gen.writeNumberField(OPEN, price.getOpen());
    gen.writeNumberField(HIGH, price.getHigh());
    gen.writeNumberField(LOW, price.getLow());
    gen.writeNumberField(CLOSE, price.getClose());
    gen.writeNumberField(CHANGE, price.getChange());
    gen.writeNumberField(VOLUME, price.getVolume());
    gen.writeEndObject();
  }

  @Override
  public void serializeWithType(final Price price,
                                final JsonGenerator gen,
                                final SerializerProvider provider,
                                final TypeSerializer typeSer) throws IOException {
    serialize(price, gen, provider);
  }
}
