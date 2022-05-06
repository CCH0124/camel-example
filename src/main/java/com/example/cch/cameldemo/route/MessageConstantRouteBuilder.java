package com.example.cch.cameldemo.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MessageConstantRouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("file:src/test/src-folder?fileName=constant.txt")
        .log("received a file")
        .transform(constant("constant!"))
        .setBody(constant("setBody!"))
        .transform(simple("Hello, ${body}!"))
        .setHeader("CamelFileName", constant("constant.txt"))
        .to("file:src/test/des-folder/output");

  }

}
