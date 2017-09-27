package com.bn.ninjatrader.service.annotation.cached;

import com.bn.ninjatrader.common.type.Role;
import com.google.inject.BindingAnnotation;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks that the List of DailyQuotes are cached. Any updates to the list will be reflected in the cache.
 *
 * @author bradwee2000@gmail.com
 */
@NameBinding
@BindingAnnotation
@Retention(RUNTIME)
@Target({FIELD, PARAMETER, METHOD})
public @interface DailyQuotesCache {
   Role[] value() default {};
}
