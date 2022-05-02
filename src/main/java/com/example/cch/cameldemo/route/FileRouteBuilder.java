package com.example.cch.cameldemo.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Add to Camel context
 */
@Component
public class FileRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
        from("file:"+ "src/test/src-folder" + "?noop=true")
        .log("Received...")
        .to("file:"+ "src/test/des-folder");
    }
    
}
