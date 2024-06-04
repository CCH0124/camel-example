package com.example.cch.cameldemo.service.impl;

import com.example.cch.cameldemo.entity.Employee;
import com.example.cch.cameldemo.service.EmployeeService;

import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  @Override
  public void salaryProcess(Employee employee) {
    // TODO Auto-generated method stub
    employee.setSalary(employee.getSalary()+20);
  }
  
}
