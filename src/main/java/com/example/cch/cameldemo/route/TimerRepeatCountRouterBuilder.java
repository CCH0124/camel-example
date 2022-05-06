package com.example.cch.cameldemo.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TimerRepeatCountRouterBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("timer:onceTimer?repeatCount=3&period=5000")
        .log("Executes just once!");
  }

}
