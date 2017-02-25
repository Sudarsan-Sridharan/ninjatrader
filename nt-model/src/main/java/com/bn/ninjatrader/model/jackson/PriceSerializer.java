package com.bn.ninjatrader.model.jackson;

import com.bn.ninjatrader.model.entity.Price;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.inject.Singleton;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceSerializer extends StdSerializer<Price> {

  public PriceSerializer() {
    this(Price.class);
  }

  public PriceSerializer(Class<Price> t) {
    super(t);
  }

  @Override
  public void serialize(final Price price,
                        final JsonGenerator gen,
                        final SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("d", price.getDate().format(DateTimeFormatter.BASIC_ISO_DATE));
    gen.writeNumberField("o", price.getOpen());
    gen.writeNumberField("h", price.getHigh());
    gen.writeNumberField("l", price.getLow());
    gen.writeNumberField("c", price.getClose());
    gen.writeNumberField("ch", price.getChange());
    gen.writeNumberField("v", price.getVolume());
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
