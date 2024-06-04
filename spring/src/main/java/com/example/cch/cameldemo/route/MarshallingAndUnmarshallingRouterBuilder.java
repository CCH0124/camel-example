package com.example.cch.cameldemo.route;

import org.apache.camel.Exchange;
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
        .setHeader(Exchange.FILE_NAME, constant("out1.json")) //此行也可定義輸出檔案
        .to("file:src/test/des-folder/json");
  }

}
