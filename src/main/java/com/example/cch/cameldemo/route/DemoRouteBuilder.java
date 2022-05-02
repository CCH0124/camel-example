package com.example.cch.cameldemo.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DemoRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
        from("timer:hello?period={{timer.period}}")
                .setBody(simple("{{greeting.word}}, Hello from timer!"))
                .to("log:out");
    }

}
