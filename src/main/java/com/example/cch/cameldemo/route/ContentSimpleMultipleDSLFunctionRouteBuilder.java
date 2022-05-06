package com.example.cch.cameldemo.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ContentSimpleMultipleDSLFunctionRouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("file:src/test/src-folder?fileName=simple2.txt")
    .log(">>> ${body}")
      .choice()
        .when(simple("${body} == 'Hello, world!'"))
          .transform(simple("Hello, everyone!"))
          .log(">>> Transform: ${body}")
          .to("file:src/test/des-folder")
        .otherwise()
          .to("file:src/test/trash");
  }
  
}
