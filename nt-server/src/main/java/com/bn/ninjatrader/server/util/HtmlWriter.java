package com.bn.ninjatrader.server.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;

/**
 * @author bradwee2000@gmail.com
 */
public class HtmlWriter {
  private static final String CHARSET = "UTF-8";

  public static final HtmlWriter withTemplatePath(final String templatePath) {
    return new HtmlWriter(templatePath);
  }

  private final String templatePath;
  private final VelocityContext context = new VelocityContext();

  private HtmlWriter(final String templatePath) {
    this.templatePath = templatePath;
  }

  public HtmlWriter put(final String key, final Object value) {
    context.put(key, value);
    return this;
  }

  public String write() {
    final StringWriter stringWriter = new StringWriter();
    Velocity.mergeTemplate(templatePath, CHARSET, context, stringWriter);
    return stringWriter.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HtmlWriter that = (HtmlWriter) o;
    return Objects.equal(templatePath, that.templatePath) &&
        Objects.equal(context, that.context);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(templatePath, context);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("templatePath", templatePath)
        .add("context", context)
        .toString();
  }
}
