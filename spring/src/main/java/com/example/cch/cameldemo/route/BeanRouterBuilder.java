package com.example.cch.cameldemo.route;

import com.example.cch.cameldemo.service.impl.MyBeanServiceImpl;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BeanRouterBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("direct:start")
        .bean(new MyBeanServiceImpl())
        .log("The message body is now: ${body}");

  }

}
