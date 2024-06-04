package com.example.cch.cameldemo.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MethodRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
        from("timer:hello?period={{timer.period}}&delay=2000").routeId("hello")
                .transform().method("myBeanServiceImpl", "saySomething")
                .log(">>> ${body}")
                .filter(simple("${body} contains 'foo'"))
                .to("log:foo")
                .end()
                .to("stream:out"); // System.in、System.out、System.err
    }

}
