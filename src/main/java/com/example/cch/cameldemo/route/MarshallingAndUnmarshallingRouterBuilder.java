package com.example.cch.cameldemo.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MarshallingAndUnmarshallingRouterBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("file:src/test/src-folder/?noop=true&fileName=test.csv")
        .routeId("marshalling")
        .unmarshal().csv()
        .log(">>> ${body}")
        .marshal().json()
        .to("file:src/test/des-folder/json?fileName=out.json");
  }

}
