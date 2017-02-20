package com.bn.ninjatrader.service.appengine.filter;

import com.googlecode.objectify.ObjectifyFilter;

import javax.servlet.annotation.WebFilter;

/**
 * @author bradwee2000@gmail.com
 */
@WebFilter(filterName="ObjectifyFilter", urlPatterns = "/*")
public class AppEngineObjectifyFilter extends ObjectifyFilter {

}
