package com.bn.ninjatrader.service.annotation;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks that a method requires specific roles.
 * This is used in REST services.
 *
 * @author bradwee2000@gmail.com
 */
@NameBinding
@Retention(RUNTIME)
@Target({METHOD})
public @interface Event {

   Class messageClass();

   int[] statusCodes() default {200};
}
