package com.example.cch.cameldemo.route;

import java.util.ArrayList;
import java.util.List;

import com.example.cch.cameldemo.entity.Department;
import com.example.cch.cameldemo.entity.Employee;
import com.example.cch.cameldemo.service.EmployeeService;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SplitDepartmentRouterBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    // TODO Auto-generated method stub
    from("timer://generateEmployee?fixedRate=true&period=10000")
        .log("Generate Department...")
        .process(new Processor() {

          public void process(Exchange exchange) throws Exception {
            Department order = new Department();
            Employee e1 = new Employee("1", "Itachi", "N500", 20.0);
            Employee e2 = new Employee("2", "Naruto", "N501", 22.0);
            Employee e3 = new Employee("3", "Madara", "N501", 24.0);
            Employee e4 = new Employee("4", "Kevin", "N500", 15.0);
            List<Employee> list = new ArrayList<>();
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            order.setEmployees(list);
            exchange.getIn().setBody(order.getEmployees());
          }
        })
        .to("direct:processDepartment");

    from("direct:processDepartment")
        .log("Process Department ${body}")
        .split(simple("${body}"))
        .to("direct:processEmployeeItem")
        .log("Processing done ${body}")
        .end()
        .log("Employee raise processed: ${body}");

    from("direct:processEmployeeItem")
        .log("Processing item ${body.name}")
        .bean(EmployeeService.class, "salaryProcess(${body})");
  }

}
