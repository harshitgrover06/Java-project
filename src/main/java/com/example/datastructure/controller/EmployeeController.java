package com.example.datastructure.controller;

import com.example.datastructure.dto.EmployeeDTO;
import com.example.datastructure.dto.EmployeeDTOConverter;
import com.example.datastructure.model.Employee;
import com.example.datastructure.model.Grade;
import com.example.datastructure.service.CsvService;
import com.example.datastructure.service.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CsvService csvService;

    @Transactional
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDTO> employeeDTOs = employees.stream()
                .map(EmployeeDTOConverter::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(employeeDTOs);
    }

    @PostMapping("/employees/import_csv")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a CSV file");
        }

        try {
            csvService.processCsv(file);
            return ResponseEntity.ok("CSV file imported successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error importing CSV file");
        }
    }

    @GetMapping("/employees/export_as_csv")
    public ResponseEntity<byte[]> exportToCsv() {
        try {
            // Fetch employee data from service layer
            List<Employee> employees = employeeService.getAllEmployees();

            // Create ByteArrayOutputStream to hold CSV data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);

            // Call exportToCsv method to write CSV data to PrintWriter
            csvService.exportToCsv(employees, writer);
            writer.flush();
            writer.close();

            // Convert CSV data to byte array
            byte[] csvBytes = baos.toByteArray();

            // Set response headers for CSV download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("filename", "employees.csv");

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/employee/{id}/{grade}")
    public ResponseEntity<String> updateEmployeeGrade(
            @PathVariable("id") Long employeeId,
            @PathVariable("grade") Grade newGrade) {
        try {
            // Check if employee exists
            Employee existingEmployee = employeeService.getEmployeeById(employeeId);
            if (existingEmployee == null) {
                return ResponseEntity.notFound().build();
            }

            // Update employee grade
            existingEmployee.setGrade(newGrade);
            employeeService.updateEmployee(existingEmployee);

            return ResponseEntity.ok("Employee grade updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating employee grade");
        }
    }
}
