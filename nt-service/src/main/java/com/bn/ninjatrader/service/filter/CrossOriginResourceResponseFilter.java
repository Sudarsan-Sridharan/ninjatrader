package com.bn.ninjatrader.service.filter;

import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class CrossOriginResourceResponseFilter implements ContainerResponseFilter {
  @Override
  public void filter(final ContainerRequestContext req,
                     final ContainerResponseContext res) throws IOException {

    res.getHeaders().add("Access-Control-Allow-Origin", "*");
    res.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    res.getHeaders().add("Access-Control-Allow-Credentials", "true");
    res.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
  }
}
