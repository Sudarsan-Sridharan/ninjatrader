package com.bn.ninjatrader.worker.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class TaskSerializer implements StreamSerializer<Object> {

  private static final Logger LOG = LoggerFactory.getLogger(TaskSerializer.class);

  private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
    final Kryo kryo = new Kryo();
    kryo.register(Callable.class);
    kryo.register(Runnable.class);
    return kryo;
  });

  public int getTypeId() {
    return 5;
  }

  public void write(ObjectDataOutput out, Object object)
      throws IOException {
    final Kryo kryo = kryoThreadLocal.get();
    final Output output = new Output((OutputStream) out);
    kryo.writeClassAndObject(output, object);
    output.flush();
  }

  public Object read(ObjectDataInput in) throws IOException {
    final Kryo kryo = kryoThreadLocal.get();
    final Input input = new Input((InputStream) in);
    final Object o = kryo.readClassAndObject(input);
    return o;
  }

  public void destroy() {
  }
}
