package com.example.datastructure.service;

import com.example.datastructure.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    void updateEmployee(Employee employee);
    List<Employee> searchEmployeesByFirstName(String firstName);
}
