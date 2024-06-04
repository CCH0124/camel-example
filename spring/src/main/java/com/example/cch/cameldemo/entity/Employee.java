package com.example.cch.cameldemo.entity;

public class Employee {
  private String id;

  private String name;

  private String department;

  private double salary;

  public Employee(String id, String name, String department, double salary) {
    this.id = id;
    this.name = name;
    this.department = department;
    this.salary = salary;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  @Override
  public String toString() {
    return "Employee [department=" + department + ", id=" + id + ", name=" + name + ", salary=" + salary + "]";
  }

  
}
