/*
 * Copyright (c) 2016 Practice Insight Pty Ltd.  All rights reserved.
 */

package com.bn.ninjatrader.model.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
public @interface TestCollection {

}
