package com.example.datastructure.service;

import com.example.datastructure.dto.EmployeeDTO;
import com.example.datastructure.model.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.io.PrintWriter;
import java.util.List;

public interface CsvService {
    void processCsv(MultipartFile file);
    void exportToCsv(List<Employee> employees, PrintWriter writer);
}
