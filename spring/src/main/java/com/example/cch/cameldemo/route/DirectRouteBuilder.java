package com.example.cch.cameldemo.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DirectRouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("direct:process-file")
        .choice()
        .when(simple("${bodyAs(String)} contains 'Cilla Black'"))
        .log(">>> ${body}")
        .to("file:src/test/des-folder/cilla_black")
        .end();

    from("file:src/test/trash?fileName=direct.txt")
        .log(">>> trash: ${body}")
        .to("direct:process-file");
    from("file:src/test/src-folder?fileName=direct.txt")
        .log(">>> src-folder: ${body}")
        .to("direct:process-file");
  }

}
