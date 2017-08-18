package com.bn.ninjatrader.service.annotation.cached;

import com.bn.ninjatrader.common.type.Role;
import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a Map of {@link com.bn.ninjatrader.simulation.scanner.ScanResult} as cached.
 * Any updates to the map will be reflected in the cache.
 *
 * @author bradwee2000@gmail.com
 */
@BindingAnnotation
@Retention(RUNTIME)
@Target({FIELD, PARAMETER, METHOD})
public @interface CachedScanResults {
   Role[] value() default {};
}
