package com.example.datastructure.service;

import com.example.datastructure.model.Employee;
import com.example.datastructure.model.Grade;
import com.example.datastructure.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CsvServiceImplementation implements CsvService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void processCsv(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Employee employee = new Employee();
                employee.setFirstName(data[0]);
                employee.setLastName(data[1]);
                employee.setDateOfBirth(parseDate(data[2])); // Assuming date format is yyyy-MM-dd
                employee.setDateOfJoining(parseDate(data[3])); // Assuming date format is yyyy-MM-dd
                employee.setGrade(Grade.valueOf(data[4].trim())); // Assuming Grade is an Enum
                employeeRepository.save(employee);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO exception
        }
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr.trim(), formatter);
    }

    public void exportToCsv(List<Employee> employees, PrintWriter writer) {
        // Write CSV header
        writer.println("ID,First Name,Last Name,Date of Birth,Date of Joining,Grade");

        // Write employee data
        for (Employee employee : employees) {
            StringBuilder sb = new StringBuilder();
            sb.append(employee.getId()).append(",");
            sb.append(StringUtils.quote(employee.getFirstName())).append(",");
            sb.append(StringUtils.quote(employee.getLastName())).append(",");
   
