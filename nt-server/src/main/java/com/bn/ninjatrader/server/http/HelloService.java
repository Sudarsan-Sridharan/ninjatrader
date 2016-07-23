package com.bn.ninjatrader.server.http;

import com.google.sitebricks.At;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

/**
 * Created by Brad on 4/26/16.
 */
@At("/hello")
@Service
public class HelloService {

  @Get
  public Reply<?> hello() {
    return Reply.with("Hello World");
  }
}
